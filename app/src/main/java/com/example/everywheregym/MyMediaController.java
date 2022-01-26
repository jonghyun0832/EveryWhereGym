package com.example.everywheregym;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

public class MyMediaController extends MediaController {

    Context context;

    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }

    public MyMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);

        this.context = context;
    }

    public MyMediaController(Context context) {
        super(context);

        this.context = context;
    }
}
