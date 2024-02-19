<template>
  <div class="userCenter top-form">
    <el-form ref="form" :model="form" label-position="top" size="medium">
      <el-form-item label="账号">
        <el-input v-model="form.userId" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickName"></el-input>
      </el-form-item>
      <el-form-item label="头像">
        <el-upload
          class="avatar-uploader"
          action="''"
          :show-file-list="false"
          :before-upload="beforeAvatarFileUpload"
          :http-request="avatarFileUpload">
          <img v-if="form.avatarFilePath" :src="form.avatarFilePath" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="saveBtnClick">保 存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import api_rbac from "../api/rbac";
  import glob from "../assets/js/glob";
  import api_file from "../api/file";

  export default {
    name: "userCenter",
    data() {
      return {
        elUserCenter: {},
        form: {}
      }
    },
    methods: {
      //初始化
      init() {
        this.reset();
        this.initAsync();
      },
      //初始化(异步)
      async initAsync() {
        //获得用户信息
        const loading = this.$loading({target: this.elUserCenter, lock: true,});
        api_rbac.infoByToken(glob.getToken()).then(response => {
          this.form = response.data.result;
        }).finally(() => loading.close());
      },
      //重置
      reset() {
        this.elUserCenter = document.getElementsByClassName('userCenter')[0];
        this.form = {};
      },
      //保存按钮点击事件
      saveBtnClick() {
        //保存
        const loading = this.$loading({target: this.elUserCenter, lock: true,});
        api_rbac.save(this.form).then(response => {
          window.location.href = `${process.env.VUE_APP_ADMIN_BASE_URL}`;
        }).finally(() => loading.close());
      },
      //头像上传
      beforeAvatarFileUpload(file) {
        const isType = file.type === 'image/jpeg' || file.type === 'image/png';
        const isSize = file.size / 1024 / 1024 < 1;
        if (!isType) {
          this.$message.error('上传头像图片只能是JPG/PNG格式!');
        }
        if (!isSize) {
          this.$message.error('上传头像图片大小不能超过1MB!');
        }
        return isType && isSize;
      },
      //头像上传
      avatarFileUpload(fileData) {
        //构造参数
        const data = new FormData()
        data.append('rootPathCode', 'common');
        data.append('file', fileData.file);
        data.append('genPreviewPdfFile', false);
        //上传
        const loading = this.$loading({target: this.elUserCenter, lock: true,});
        api_file.upload(data).then(response => {
          const fileInfo = response.data.result;
          this.form.avatarFileId = fileInfo.id;
          this.form.avatarFilePath = '/file/' + fileInfo.rootPathCode + '/' + fileInfo.physicalRelativePath + '/' + fileInfo.physicalFileName;
        }).finally(() => loading.close());
      }
    }
  }
</script>

<style lang="scss" scoped>
  .userCenter {
    padding: 0px 20px;

    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 100px;
      height: 100px;
      line-height: 100px;
      text-align: center;
    }

    .avatar {
      width: 100px;
      height: 100px;
      display: block;
    }

  }

</style>
