package net.hungerstruck.renaissance.countdown

class CountdownManager {
    private val runningCountdowns: MutableList<CountdownWrapper> = arrayListOf()

    public fun start(countdown: Countdown, time: Int) {
        val wrapper = CountdownWrapper(countdown)
        wrapper.start(time)
        runningCountdowns.add(wrapper)
    }

    public fun cancel(clazz: Class<out Countdown>) {
        runningCountdowns.removeAll {
            if (it.javaClass == clazz) {
                it.cancel()
                true
            } else false
        }
    }

    public fun cancelAll() {
        runningCountdowns.removeAll {
            it.cancel()
            true
        }
    }

    public fun hasCountdown(clazz: Class<out Countdown>) = runningCountdowns.filter { it.countdown.javaClass == clazz }.any()
}