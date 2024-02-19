const api_file = {
  upload: (formData = {}) => axios.post('/private/file/upload', formData, {'Content-Type': 'multipart/form-data'})
};
export default api_file;
