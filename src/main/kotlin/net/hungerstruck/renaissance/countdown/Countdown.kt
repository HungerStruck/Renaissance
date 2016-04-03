package net.hungerstruck.renaissance.countdown

abstract class Countdown {
    open fun onStart(timeLeft: Int) {}
    abstract fun onTick(timeLeft: Int)
    abstract fun onFinish()
    open fun onCancel() {}
}