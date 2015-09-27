这是一个带有“粘性”功能的viewpager。<br/><br/>
ViewPager带有粘性功能，常规的处理方法可能会超级复杂，因为涉及到大量的onTouch监听、拦截，滑动时因为效率问题影响用户体验，不信你去看一下应用宝、豌豆荚的App详情页，然后滑动一下试试。<br><br>
但是这个demo却使用了很讨巧的方法，里面很少看到onTouch拦截、事件消费的逻辑处理。细看代码你会发现，这个demo使用了障眼法，很巧妙的障眼法。这个障眼法的灵感，来源于另外一款App的处理逻辑，我在滑动时不小心琢磨到了。我没有去反编译，没有去看代码，却猜到了大体的框架，这真是一件很有意思的事情。<br><br>
viewpager左右滑动的时候，始终有一个view“粘”在顶部。Viewpager左边fragment是scrollView，右边是listview。左右两个fragment上下滑动的时候，都会计算并动态调整stickyView的位置。在viewpager左右滑动的时候，两个fragment的stickyView高度之间的契合也做了调整。在豌豆荚和应用宝的app详情页界面，对stickyView的处理太过生硬，用户体验不太友好。可以参考这个demo的实现方案。<br><br>
先上两张图如下：<br><br>
<td>
  <img src="gif01.gif" width="300" height="500" />
  <img src="gif02.gif" width="300" height="500" style="margin-left:50px" />
</td>

viewpager在上下滑动的时候，对stickyview位置的改变，会存在惯性。大多bug已经修复完成，滑动比较流畅。欢迎拍砖~<br>

备注：<br>
该project使用的水平listview是：https://github.com/MeetMe/Android-HorizontalListView --(只修改了dispatchTouchEvent方法)<br>
viewpager指示器是：https://github.com/astuetz/PagerSlidingTabStrip<br><br>

####后记
该project提供的思路，最近被作为公司App某个页面的主框架。那个页面，会从网络拉取数据，业务稍显复杂，而且listView还添加了Footer。修改的过程比较曲折，有2个一定要思考的细节点如下：<br>
(1) 网络数据获取失败时，viewPager的header如何实现黏性滑动?<br>
(2) 网络数据获取成功，但是listView.getCount()较小，使得所有getView的高度之和，依然无法将stickyView撑到最顶部，则黏性滑动无法完整，这该怎么做?<br><br>
这是2个比较严肃的问题，如果解决不好，则给用户的一种感觉就是：“半成品”，恐怕boss们也不会允许产品上线和发布。<br>
但是解决这2个问题，是很有挑战性的，不是么？<br>
友情提示：黏性View的位移处理可以非常精确，这取决于占位View的高度计算是否精准。事实上，ListView的scroll区间、黏性View的高度和footerView的高度都能被计算的很精确，占位View的高度计算，又能有多麻烦呢？


