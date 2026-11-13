
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import {
    login,
    getCurrentUser,
    logout,
} from "./authService";

const extractUser = (userDetails) => {
    const authorities = userDetails.authorities || [];

    const roleAuthority = authorities
        .map((authority) => authority.authority)
        .find((authority) =>
            ["ROLE_STUDENT", "ROLE_TEACHER", "ROLE_ADMIN",
             "STUDENT", "TEACHER", "ADMIN"].includes(authority)
        );

    if (!roleAuthority) {
        throw new Error("Unable to determine user role");
    }

    return {
        email: userDetails.username,
        role: roleAuthority.replace("ROLE_", ""),
    };
};

export const loginUser = createAsyncThunk(
    "auth/login",
    async (credentials, { rejectWithValue }) => {
        try {
            await login(credentials);

            const userDetails = await getCurrentUser();

            return extractUser(userDetails);
        } catch (error) {
            return rejectWithValue(
                error.response?.data?.message ||
                error.message ||
                "Login failed"
            );
        }
    }
);

export const initializeAuth = createAsyncThunk(
    "auth/initialize",
    async (_, { rejectWithValue }) => {
        try {
            const userDetails = await getCurrentUser();
            return extractUser(userDetails);
        } catch (error) {
            return rejectWithValue(error.message || "Not authenticated");
        }
    }
);

export const logoutUser = createAsyncThunk(
    "auth/logout",
    async (_, { rejectWithValue }) => {
        try {
            await logout();
        } catch (error) {
            return rejectWithValue(
                error.response?.data?.message || "Logout failed"
            );
        }
    }
);

const initialState = {
    user: null,
    isAuthenticated: false,
    initialized: false,
    loading: false,
    error: null,
};

const authSlice = createSlice({
    name: "auth",
    initialState,

    reducers: {
        clearAuthError: (state) => {
            state.error = null;
        },
    },

    extraReducers: (builder) => {
        builder
            // Login
            .addCase(loginUser.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(loginUser.fulfilled, (state, action) => {
                state.loading = false;
                state.user = action.payload;
                state.isAuthenticated = true;
                state.initialized = true;
            })
            .addCase(loginUser.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
                state.isAuthenticated = false;
                state.user = null;
            })

            // Restore authentication after refresh
            .addCase(initializeAuth.fulfilled, (state, action) => {
                state.user = action.payload;
                state.isAuthenticated = true;
                state.initialized = true;
            })
            .addCase(initializeAuth.rejected, (state) => {
                state.user = null;
                state.isAuthenticated = false;
                state.initialized = true;
            })

            // Logout
            .addCase(logoutUser.fulfilled, (state) => {
                state.user = null;
                state.isAuthenticated = false;
                state.initialized = true;
                state.error = null;
            })
            .addCase(logoutUser.rejected, (state) => {
                state.user = null;
                state.isAuthenticated = false;
                state.initialized = true;
            });
    },
});

export const { clearAuthError } = authSlice.actions;

export default authSlice.reducer;
