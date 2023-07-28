<template>
  <div id="app" :style="appStyle">
    <el-container :style="mainElContainerStyle">
      <el-header :style="mainElHeaderStyle" :height="mainElHeaderStyle.height">
        <el-button class="mainMenuBtn" :style="mainMenuBtnStyle" type="text">
          <i class="el-icon-menu"></i>
        </el-button>
        <a class="mainLogoBtn">
          <span>
            <i>{{mainLogo.appLogoName}}</i>
            <i>{{mainLogo.appName}}</i>
          </span>
        </a>
        <div class="mainMenuRightDiv">
          <div class="bellDiv">
            <el-badge is-dot>
              <i class="el-icon-bell"></i>
            </el-badge>
          </div>
          <div class="avatarDiv">
            <el-avatar shape="square" size="large" icon="el-icon-user-solid">
            </el-avatar>
          </div>
        </div>
      </el-header>
      <el-scrollbar style="height:100%">
        <el-backtop target=".el-scrollbar__wrap">
          <i class="el-icon-caret-top"></i>
        </el-backtop>
        <el-main>
          main
        </el-main>
      </el-scrollbar>
    </el-container>
  </div>
</template>

<script>
  export default {
    name: 'App',
    data() {
      return {
        appStyle: {
          background: "#F2F2F2"
        },
        mainLogo: {
          appLogoName: 'UDF',
          appName: 'ADMIN',
        },
        mainElContainerStyle: {
          height: 0
        },
        mainElHeaderStyle: {
          height: "50",
          background: "rgb(255, 255, 255)"
        },
        mainMenuBtnStyle: {
          background: "none rgb(255, 106, 0)",
          color: "rgb(255, 255, 255)"
        }
      }
    },
    created() {
      window.addEventListener('resize', this.getMainElContainerStyleHeight);
      this.getMainElContainerStyleHeight();
    },
    methods: {
      getMainElContainerStyleHeight() {
        this.mainElContainerStyle.height = window.innerHeight + 'px';
      }
    }
  }
</script>
