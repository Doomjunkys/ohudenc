<template>
  <div class="leftMenu">
    <el-menu background-color="#F5F5F5" @select="leftMenuSelect">
      <el-menu-item index="homeabc">
        <i class="el-icon-menu"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <div v-if="data">
        <left-menu-item :data="data"></left-menu-item>
      </div>
    </el-menu>
  </div>
</template>

<script>
  import leftMenuItem from "./leftMenuItem";

  export default {
    name: "leftMenu",
    components: {
      leftMenuItem
    },
    props: {
      data: {
        type: Array,
        required: false,
        default: () => []
      }
    },
    methods: {
      //菜单激活回调
      leftMenuSelect(index, indexPath) {
        if (this.$route.name !== index) {
          this.$router.push({name: index});
        }
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
