<template>
  <div class="leftMenuItem">
    <!-- 菜单 -->
    <div v-for="item of data" :key="item.code">
      <el-submenu :index="item.code">
        <template slot="title">
          <i :class="item.className"></i>
          <span>{{item.name}}</span>
        </template>
        <!-- 子菜单 -->
        <div v-if="item.childMenus">
          <left-menu-item :data="item.childMenus"></left-menu-item>
        </div>
        <!-- 子功能 -->
        <div v-if="item.childFunctions">
          <div v-for="item of item.childFunctions" :key="item.code">
            <el-menu-item :index="item.code">
              <template slot="title">
                <span>{{item.name}}</span>
              </template>
            </el-menu-item>
          </div>
        </div>
      </el-submenu>
    </div>
  </div>
</template>

<script>

  export default {
    name: "leftMenuItem",
    props: {
      data: {
        type: Array,
        required: false,
        default: () => []
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
