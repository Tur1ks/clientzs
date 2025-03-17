package ravage.client.util.animation;

public class Animation {
    private float start;
    private float end;
    private float progress;
    private float speed;
    private AnimationType type;
    private boolean finished;

    public enum AnimationType {
        LINEAR,
        EASE_IN,
        EASE_OUT,
        EASE_IN_OUT,
        BOUNCE
    }

    public Animation(float start, float end, float speed, AnimationType type) {
        this.start = start;
        this.end = end;
        this.speed = speed;
        this.type = type;
        this.progress = 0;
        this.finished = false;
    }

    public void reset() {
        this.progress = 0;
        this.finished = false;
    }

    public float getValue() {
        float delta = end - start;
        float value = 0;

        switch (type) {
            case LINEAR:
                value = start + (delta * progress);
                break;
            case EASE_IN:
                value = start + (delta * progress * progress);
                break;
            case EASE_OUT:
                value = start + (delta * (1 - (1 - progress) * (1 - progress)));
                break;
            case EASE_IN_OUT:
                value = (float) (start + (delta * (progress < 0.5 ? 2 * progress * progress : 1 - Math.pow(-2 * progress + 2, 2) / 2)));
                break;
            case BOUNCE:
                if (progress < 0.5) {
                    value = start + (delta * (8 * progress * progress * progress * progress));
                } else {
                    float p = progress - 1;
                    value = start + (delta * (1 - 8 * p * p * p * p));
                }
                break;
        }

        return value;
    }

    public void update() {
        if (!finished) {
            progress += speed;
            if (progress >= 1) {
                progress = 1;
                finished = true;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public void setType(AnimationType type) {
        this.type = type;
    }
}