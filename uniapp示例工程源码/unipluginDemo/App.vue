<script>
	var gaodeLocations = uni.requireNativePlugin("leehor-gaode-location")

	export default {
		data() {
			return {
			
			}
		},
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			getGaode(){
				return this.gaodeLocations
			}
		}
	}

</script>

<style>
	/*每个页面公共css */
	@import './common/uni-nvue.css';
</style>
