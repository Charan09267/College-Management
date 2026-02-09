 
import { createSlice } from "@reduxjs/toolkit";

const studentSlice = createSlice({
  name: "student",
  initialState: {
    profile: null,
  },
  reducers: {
    setStudent: (state, action) => {
      state.profile = action.payload;
    },
    clearStudent: (state) => {
      state.profile = null;
    },
  },
});

export const { setStudent, clearStudent } = studentSlice.actions;
export default studentSlice.reducer;
