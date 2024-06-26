<template>
	<view>
		<u-navbar :is-fixed="true" :is-back="false" title-color="#fff" :background="{'background-image': 'linear-gradient(45deg, rgb(28, 187, 180), rgb(141, 198, 63))'}">
			<view class="slot-wrap">
				<view class="search-wrap">
					<!-- 如果使用u-search组件，必须要给v-model绑定一个变量 -->
					<u-search height="56" :show-action="false" :action-style="{color: '#fff'}"></u-search>
				</view>
			</view>
			<view class="navbar-right" slot="right">
				<view class="message-box right-item">
					<u-icon name="chat" size="56"></u-icon>
					<!-- <u-badge count="18" size="mini" :offset="[-15, -15]"></u-badge> -->
				</view>
				<view class="dot-box right-item">
					<u-icon name="calendar-fill" size="56"></u-icon>
					<!-- <u-badge size="mini" :is-dot="true" :offset="[-6, -6]"></u-badge> -->
				</view>
			</view>
		</u-navbar>
		<view class="container">
			<u-button>按钮</u-button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				href: 'https://uniapp.dcloud.io/component/README?id=uniui'
			}
		},
		methods: {

		}
	}
</script>

<style>
	.container {
		padding: 20px;
		font-size: 14px;
		line-height: 24px;
	}
</style>

<style lang="scss" scoped>
	.slot-wrap {
		display: flex;
		align-items: center;
		flex: 1;
	}

	.search-wrap {
		margin: 0 20rpx;
		flex: 1;
	}

	.navbar-right {
		margin-right: 10rpx;
		display: flex;
	}

	.right-item {
		margin: 0 10rpx;
		position: relative;
		color: #ffffff;
		display: flex;
	}
</style>
