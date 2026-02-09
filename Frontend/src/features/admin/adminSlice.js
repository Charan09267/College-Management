import { createSlice } from "@reduxjs/toolkit";

const adminSlice = createSlice({
  name: "admin",
  initialState: {
    email: null,
    role: null, 
  },
  reducers: {
    setAdmin: (state, action) => {
      state.email = action.payload.email;
      state.role = action.payload.role;
    },
    clearAdmin: (state) => {
      state.email = null;
      state.role = null;
    },
  },
});

export const { setAdmin, clearAdmin } = adminSlice.actions;
export default adminSlice.reducer;
