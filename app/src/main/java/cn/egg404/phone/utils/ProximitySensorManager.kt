package cn.egg404.phone.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


/**
 * Manages the proximity sensor and notifies a listener when enabled.
 */
class ProximitySensorManager(context: Context, listener: Listener) {
    /**
     * Listener of the state of the proximity sensor.
     *
     *
     * This interface abstracts two possible states for the proximity sensor, near and far.
     *
     *
     * The actual meaning of these states depends on the actual sensor.
     */
    interface Listener {
        /** Called when the proximity sensor transitions from the far to the near state.  */
        fun onNear()

        /** Called when the proximity sensor transitions from the near to the far state.  */
        fun onFar()
    }

    enum class State {
        NEAR, FAR
    }

    private var mProximitySensorListener: ProximitySensorEventListener? = null

    /**
     * The current state of the manager, i.e., whether it is currently tracking the state of the
     * sensor.
     */
    private var mManagerEnabled = false

    /**
     * The listener to the state of the sensor.
     *
     *
     * Contains most of the logic concerning tracking of the sensor.
     *
     *
     * After creating an instance of this object, one should call [.register] and
     * [.unregister] to enable and disable the notifications.
     *
     *
     * Instead of calling unregister, one can call [.unregisterWhenFar] to unregister the
     * listener the next time the sensor reaches the [State.FAR] state if currently in the
     * [State.NEAR] state.
     */
    private class ProximitySensorEventListener(
        private val mSensorManager: SensorManager,
        private val mProximitySensor: Sensor,
        listener: Listener
    ) :
        SensorEventListener {
        private val mMaxValue: Float
        private val mListener: Listener

        /**
         * The last state of the sensor.
         *
         *
         * Before registering and after unregistering we are always in the [State.FAR] state.
         */
        private var mLastState: State

        /**
         * If this flag is set to true, we are waiting to reach the [State.FAR] state and
         * should notify the listener and unregister when that happens.
         */
        private var mWaitingForFarState: Boolean
        override fun onSensorChanged(event: SensorEvent) {
            // Make sure we have a valid value.
            if (event.values == null) return
            if (event.values.size == 0) return
            val value = event.values[0]
            // Convert the sensor into a NEAR/FAR state.
            val state = getStateFromValue(value)
            synchronized(this) {

                // No change in state, do nothing.
                if (state == mLastState) return
                // Keep track of the current state.
                mLastState = state
                // If we are waiting to reach the far state and we are now in it, unregister.
                if (mWaitingForFarState && mLastState == State.FAR) {
                    unregisterWithoutNotification()
                }
            }
            when (state) {
                State.NEAR -> mListener.onNear()
                State.FAR -> mListener.onFar()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // Nothing to do here.
        }

        /** Returns the state of the sensor given its current value.  */
        private fun getStateFromValue(value: Float): State {
            // Determine if the current value corresponds to the NEAR or FAR state.
            // Take case of the case where the proximity sensor is binary: if the current value is
            // equal to the maximum, we are always in the FAR state.
            return if (value > FAR_THRESHOLD || value == mMaxValue) State.FAR else State.NEAR
        }

        /**
         * Unregister the next time the sensor reaches the [State.FAR] state.
         */
        @Synchronized
        fun unregisterWhenFar() {
            if (mLastState == State.FAR) {
                // We are already in the far state, just unregister now.
                unregisterWithoutNotification()
            } else {
                mWaitingForFarState = true
            }
        }

        /** Register the listener and call the listener as necessary.  */
        @Synchronized
        fun register() {
            // It is okay to register multiple times.
            mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_UI)
            // We should no longer be waiting for the far state if we are registering again.
            mWaitingForFarState = false
        }

        fun unregister() {
            var lastState: State
            synchronized(this) {
                unregisterWithoutNotification()
                lastState = mLastState
                // Always go back to the FAR state. That way, when we register again we will get a
                // transition when the sensor gets into the NEAR state.
                mLastState = State.FAR
            }
            // Notify the listener if we changed the state to FAR while unregistering.
            if (lastState != State.FAR) {
                mListener.onFar()
            }
        }

        private fun unregisterWithoutNotification() {
            mSensorManager.unregisterListener(this)
            mWaitingForFarState = false
        }

        companion object {
            private const val FAR_THRESHOLD = 5.0f
        }

        init {
            mMaxValue = mProximitySensor.maximumRange
            mListener = listener
            // Initialize at far state.
            mLastState = State.FAR
            mWaitingForFarState = false
        }
    }

    /**
     * Enables the proximity manager.
     *
     *
     * The listener will start getting notifications of events.
     *
     *
     * This method is idempotent.
     */
    fun enable() {
        if (mProximitySensorListener != null && !mManagerEnabled) {
            mProximitySensorListener!!.register()
            mManagerEnabled = true
        }
    }

    /**
     * Disables the proximity manager.
     *
     *
     * The listener will stop receiving notifications of events, possibly after receiving a last
     * [Listener.onFar] callback.
     *
     *
     * If `waitForFarState` is true, if the sensor is not currently in the [State.FAR]
     * state, the listener will receive a [Listener.onFar] callback the next time the sensor
     * actually reaches the [State.FAR] state.
     *
     *
     * If `waitForFarState` is false, the listener will receive a [Listener.onFar]
     * callback immediately if the sensor is currently not in the [State.FAR] state.
     *
     *
     * This method is idempotent.
     */
    fun disable(waitForFarState: Boolean) {
        if (mProximitySensorListener != null && mManagerEnabled) {
            if (waitForFarState) {
                mProximitySensorListener!!.unregisterWhenFar()
            } else {
                mProximitySensorListener!!.unregister()
            }
            mManagerEnabled = false
        }
    }

    init {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        mProximitySensorListener =
            proximitySensor?.let { ProximitySensorEventListener(sensorManager, it, listener) }
    }
}
