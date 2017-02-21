# RxAnimations

[![Download](https://api.bintray.com/packages/0ximdigital/RxAnimationsRepo/RxAnimations/images/download.svg?version=0.9.1) ](https://bintray.com/0ximdigital/RxAnimationsRepo/RxAnimations/0.9.1/link)
[![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=15)

RxAnimations is a library with the main goal to make android animations more solid and cohesive.


Download
--------

```groovy
  compile 'oxim.digital:rxanim:0.9.1'
    
  compile 'io.reactivex:rxandroid:1.2.1'
  compile 'io.reactivex:rxjava:1.2.5'
```

#### RxJava version compatibility:

This RxAnimations library is only compatible with rxJava 1 greater than 1.2.x.  
If you are using rxJava 1.1.x version, take a look [here](https://github.com/0ximDigital/RxAnimations/tree/develop-rx-lt-1.2).

If you are searching for the one compatible with rxJava 2, take a look over [here](https://github.com/0ximDigital/Rx2Animations).
  
Usage
--------

A sample project which provides code examples that demonstrate uses of the classes in this
project is available in the `sample-app/` folder.

True power of this library is the ability to use animations as an observable asynchronous action.  
That enables us to apply regular rxJava APIs on the animations.

It provides a couple of classes such as `RxValueAnimator`, `RxObservableValueAnimator` and `RxAnimationBuilder`.  
Moreover, it also provides many regulary used animation methods (static import) such as `fadeIn()`, `fadeOut()`, `slideIn()`, `leave()` etc.   
  
#### Examples:

*  Animating multiple views together

```java
        animateTogether(fadeIn(firstView),
                        fadeIn(secondView));
```                 
            
*  Chaining multiple animations seamlessly

```java
        fadeIn(firstView)
            .concatWith(fadeIn(secondView));
```
            
*  Simple ValueAnimator usage with RxValueAnimator

```java
        final ValueAnimator opacityAnimator = ValueAnimator.ofFloat(0.f, 1.f);
        RxValueAnimator.from(opacityAnimator, animator -> view.setAlpha((float)animator.getAnimatedValue()))
```
            
*  Animating multiple values together with RxObservableValueAnimator

```java
        xObservableAnimator = RxObservableValueAnimator.from(xAnimator);
        yObservableAnimator = RxObservableValueAnimator.from(yAnimator);
        
        Observable.combineLatest(xObservableAnimator.schedule(),
                                 yObservableAnimator.schedule(),
                                 (first, second) -> new Pair(first, second))
                  .subscribe(this::updateView);
```

*  Defining custom animations with RxAnimationBuilder

```java
        RxAnimationBuilder.animate(view, DURATION)
                          .interpolator(INTERPOLATOR)
                          .fadeIn()
                          .rotate(DEGREES)
                          .translateBy(X, Y)
                          ...
                          .schedule([ | false]);
```

            
Animation created with `RxAnimationBuilder` automatically pretransforms the view, if not set otherwise.   
I.e. if `fadeIn()` is called, views opacity will be set to 0.f before animation starts.  
                    
           
Managing animations
--------

*Starting animation*    - Every animation chain is started when we subscribe to it.  
*Ending animation*      - Animations can be easily stopped by unsubscribing animation subscription.


## LICENSE

    Copyright 2016 Mihael Franceković

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

