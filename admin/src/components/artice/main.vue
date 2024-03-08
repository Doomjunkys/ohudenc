<template>
  <div class="main">
    <el-card class="searcBar">
      <el-button type="success" icon="el-icon-edit-outline" @click="addArticeClickHandle">写文章</el-button>
    </el-card>
    <el-card class="listBar">
      <ul v-infinite-scroll="loadData">
        <li v-for="i in count" :key="i">{{ i }}</li>
      </ul>
    </el-card>
  </div>
</template>

<script>
  import glob from "../../assets/js/glob";

  export default {
    name: "artice-main",
    data() {
      return {
        count: 0
      }
    },
    activated() {
      this.$EventBus.$on(glob.eventNames.globSearchEventName, this.globSearchHandle);
    },
    deactivated() {
      this.$EventBus.$off(glob.eventNames.globSearchEventName);
    },
    methods: {
      //全局搜索事件处理器
      globSearchHandle(searchText) {
        console.log(searchText);
      },
      //写文章点击事件
      addArticeClickHandle() {
        this.$router.push({name: 'article-add'});
      },
      //加载数据
      loadData() {
        if (this.count < 500) {
          console.log('load');
          this.count += 10;
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .main {
    padding: 10px 10px 10px 10px;

    .el-card {
      border-radius: 0px;
    }

    .searcBar {

    }

    .listBar {
      margin-top: 10px;
    }
  }
</style>
