package ioio.examples.hello_service.GuitarActivity;

/**
 * This class represent a Pointer Touch on the screen, with all it's details such as pressure, velocity etc.
 * Created by Tomer on 13/08/2016.
 */
public class PointerTouch {
    private int layoutId;
    private float velocity;
    private float pressure;
    private float y;

    public PointerTouch(int layoutId, float velocity, float pressure, float y) {
        this.layoutId = layoutId;
        this.velocity = velocity;
        this.pressure = pressure;
        this.y = y;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public float getY() {
        return y;
    }

    public float getPressure() {
        return pressure;
    }
}
