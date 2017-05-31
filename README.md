Usage
---
register
```java
RxBus.getDefault().register(this);
```

receive
```java
@Subscribe
    public void receiveEventMain(EventMain event) {
        ((TextView) findViewById(R.id.tv_main_eventMsg)).setText(event.getMsg());
    }
```
unregister
```java
    RxBus.getDefault().unregister(this);
    RxBus.getDefault().unregister(this,eventA);
```
