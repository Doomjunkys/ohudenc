import glob from "../assets/js/glob";

const api_file = {
  upload: (formData = {}) => axios.post('/private/file/upload', formData, {'Content-Type': 'multipart/form-data'}),
  preview: (fileId = "", width, height) => {
    let path = process.env.VUE_APP_BASE_URL + 'api/udf/web/private/file/preview/' + fileId + '?token=' + glob.getToken();
    if (width && height) {
      path += '&width=' + width + '&height=' + height;
    }
    return path;
  }
};
export default api_file;
