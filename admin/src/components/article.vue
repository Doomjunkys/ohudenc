<template>
  <div class="article">
    <el-card class="searcBar">
      searchBar
    </el-card>
    <el-card class="listBar">
      <ul v-infinite-scroll="load">
        <li v-for="i in count" :key="i">{{ i }}</li>
      </ul>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "article",
    data() {
      return {
        count: 0
      }
    },
    methods: {
      load() {
        if (this.count < 500) {
          console.log('load');
          this.count += 10;
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .article {
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
