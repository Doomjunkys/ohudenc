<template>
  <div id="app" class="app" :style="appStyle">
    <el-container :style="mainElContainerStyle">
      <el-header :style="mainElHeaderStyle" :height="mainElHeaderStyle.height">
        <el-button class="mainMenuBtn" :style="mainMenuBtnStyle" type="text">
          <i class="el-icon-menu"></i>
        </el-button>
        <a class="mainLogoBtn" :style="mainLogoBtnStyle">
          <span>
            <i>{{mainLogo.appLogoName}}</i>
          </span>
        </a>
        <div class="mainMenuRightDiv">
          <div class="toolDiv">
            <el-dropdown>
              <el-button icon="el-icon-s-tools" size="small" circle></el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  构建流水线
                  <a href="/jenkins/" target="view_window">(公网)</a>
                  <a href="http://web.itkk.org:81/jenkins/" target="view_window">(公网-备用)</a>
                  <a href="http://192.168.1.100/jenkins/" target="view_window">(内网)</a>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div class="login-registered-div">
            <el-button type="warning" size="small" round>登陆</el-button>
          </div>
          <div class="login-registered-div">
            <el-button type="warning" size="small" round>注册</el-button>
          </div>
<!--          <div class="bellDiv">-->
<!--            <el-badge is-dot>-->
<!--              <el-button icon="el-icon-bell" size="small" circle></el-button>-->
<!--            </el-badge>-->
<!--          </div>-->
<!--          <div class="avatarDiv">-->
<!--            <el-avatar shape="square" size="large" icon="el-icon-user-solid">-->
<!--            </el-avatar>-->
<!--          </div>-->
        </div>
      </el-header>
      <el-scrollbar style="height:100%">
        <el-backtop target=".el-scrollbar__wrap">
          <i class="el-icon-caret-top"></i>
        </el-backtop>
        <el-main>
          <transition name="el-zoom-in-center">
            <keep-alive>
              <router-view/>
            </keep-alive>
          </transition>
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
          appLogoName: 'ITKK',
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
        },
        mainLogoBtnStyle: {
          color: "rgb(255, 106, 0)"
        }
      }
    },
    created() {
      //初始化
      this.init();
    },
    methods: {
      //初始化
      init() {
        //初始化(异步)
        this.initAsync();
      },
      //初始化(异步)
      async initAsync() {
        //注册浏览器resize监听
        window.addEventListener('resize', this.getMainElContainerStyleHeight);
        //获得容器高度
        this.getMainElContainerStyleHeight();
      },
      //获得容器高度
      getMainElContainerStyleHeight() {
        this.mainElContainerStyle.height = window.innerHeight + 'px';
      }
    }
  }
</script>

<style lang="scss" scoped>
  .app {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
    font-family: 'Microsoft yahei';
    font-size: 13px;
    font-weight: 400;

    .el-container {

      .el-header {
        box-shadow: rgba(0, 0, 0, 0.08) 0px 1px 4px 0px;
        height: 50px;
        margin: 0;
        padding: 0;

        .mainMenuBtn {
          width: 50px;
          height: 50px;
          border-radius: 0px !important;
          box-sizing: border-box;
          line-height: inherit;
          cursor: pointer;
          font-family: inherit;
          font-size: inherit;
          display: inline-block;
          vertical-align: middle;
          text-align: center;
          white-space: nowrap;
          padding: 0px;
          outline: medium;
          text-decoration: none;
          transition: all 0.3s ease-out;
          border-width: initial;
          border-style: none;
          border-color: initial;
          border-image: initial;
          overflow: hidden;
          font-size: 30px;
        }

        .mainLogoBtn {
          height: 50px;
          padding: 0px 12px;
          cursor: pointer;

          span {
            font-size: 30px;
            font-weight: bold;
            font-family: inherit;
            display: inline-block;
            vertical-align: middle;
            text-align: center;
          }
        }

        .mainMenuRightDiv {
          float: right;
          height: 50px;

          .toolDiv {
            height: 50px;
            padding-right: 10px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
          }

          .login-registered-div {
            height: 50px;
            padding-right: 5px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
          }

          .avatarDiv {
            height: 50px;
            padding-right: 12px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            cursor: pointer;
          }

          .bellDiv {
            height: 50px;
            padding-right: 25px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
          }
        }
      }

      .el-main {
        margin: 0;
        padding: 0;
        overflow: hidden;
      }
    }
  }
</style>
