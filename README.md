# ChartLib
Library

### Gradle Setup

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.DiogoLino:ChartLib:1.1'
}

### Usage

1- Add a ChartView to yout Xml. you can configure it , bar colors and text.
2- Create an ArrayList of ChartItem
3- call drawChart on your chartView and pass your array, the currency and a clickListener function.
