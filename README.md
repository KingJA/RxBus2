<div align="center"><img src="res/rxbus.png"/></div>

# RxJava2

A SIMPLE library of eventbus, based on RxJava2. CLICK THE STAR if it's useful for you.

## Preview
![](res/rxbus2.gif)

## Dependencies

```groovy
compile 'io.reactivex.rxjava2:rxjava:2.1.0'
compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
```

## Usage
#### step 1: register
```java
RxBus.getDefault().register(this);
```

#### step 2: receive&post
```java
//receive
@Subscribe
public void receiveEventMain(EventMain event) {
    ((TextView) findViewById(R.id.tv_main_eventMsg)).setText(event.getMsg());
}
//post
RxBus.getDefault().post(new EventMain());
```
#### step 3: unregister
```java
    RxBus.getDefault().unregister(this);
    RxBus.getDefault().unregister(this,eventA);
```
## License

    Copyright 2017 KingJA

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
