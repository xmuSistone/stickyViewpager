这是一个带有“粘性”功能的viewpager。
viewpager左右滑动的时候，始终有一个view“粘”在顶部。在viewpager左右切换和上下滑动的时候，stickyView也会动态调整位置。
在豌豆荚和应用宝的app详情页界面，对stickyView的处理太过生硬，用户体验不太友好。可以参考这个demo的实现方案。
先上两张图如下：


![截图1](https://raw.githubusercontent.com/xmuSistone/android-sticky-viewpager/master/screen1.jpg)
![截图2](https://github.com/xmuSistone/android-sticky-viewpager/blob/master/screen2.jpg?raw=true)

viewpager在上下滑动的时候，对stickyview位置的改变，会存在惯性。大多bug已经修复完成，滑动比较流畅。欢迎拍砖~
