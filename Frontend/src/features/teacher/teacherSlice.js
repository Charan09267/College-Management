import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  profile: null, 
};

const teacherSlice = createSlice({
  name: "teacher",
  initialState,
  reducers: {
    setTeacher(state, action) {
      state.profile = action.payload;
    },
    clearTeacher(state) {
      state.profile = null;
    },
  },
});

export const { setTeacher, clearTeacher } = teacherSlice.actions;
export default teacherSlice.reducer;
