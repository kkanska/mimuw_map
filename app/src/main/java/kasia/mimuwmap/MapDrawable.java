package kasia.mimuwmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MapDrawable extends Drawable {
    private final static int ORIGINAL_WIDTH = 1080;
    private final static int ORIGINAL_HEIGHT = 277;
    private final Paint paint;
    private final Room selectedRoom;
    private final Context drawingContext;

    public MapDrawable(Context context, int color, Room room) {
        drawingContext = context;
        paint = new Paint();
        paint.setColor(color);
        selectedRoom = room;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Integer resourceId = null;
        switch (selectedRoom.getFloor()) {
            case 0:
                resourceId = R.drawable.floor_0;
                break;
            case 1:
                resourceId = R.drawable.floor_1;
                break;
            case 2:
                resourceId = R.drawable.floor_2;
                break;
            case 3:
                resourceId = R.drawable.floor_3;
                break;
            case 4:
                resourceId = R.drawable.floor_4;
                break;
            default:
                break;
        }


        int width = getBounds().width();
        float ratio = width / (float) ORIGINAL_WIDTH;
        float radius = 15 * ratio;

        if (resourceId != null) {
            Bitmap bitmap = BitmapFactory.decodeResource(drawingContext.getResources(), resourceId);
            canvas.drawBitmap(
                    Bitmap.createScaledBitmap(bitmap, width, Math.round(ORIGINAL_HEIGHT * ratio), false),
                    0, 0, new Paint());
        }

        canvas.drawCircle((float) 1.5 * selectedRoom.getLeft() * ratio,
                (float) 1.25 * selectedRoom.getTop() * ratio,
                radius,
                paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
