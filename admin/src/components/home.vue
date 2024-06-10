<template>
  <div>
    <el-card>
      <h1 class="text-center">网站正在建设中</h1>
    </el-card>
  </div>
</template>

<script>
  import glob from "../assets/js/glob";

  export default {
    name: "home",
    activated() {
      this.$EventBus.$on(glob.eventNames.globRefreshEventName, this.globRefreshHandle);
    },
    deactivated() {
      this.$EventBus.$off(glob.eventNames.globRefreshEventName);
    },
    methods: {
      //全局刷新
      globRefreshHandle() {
        this.$loading({lock: true});
        window.location.reload();
      }
    }
  }
</script>

<style lang="scss" scoped>
  .el-card {
    border-radius: 0px;
  }
</style>
