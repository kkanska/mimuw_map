package kasia.mimuwmap;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class FindProfessorResultReceiver extends ResultReceiver {

    public static final String PARAM_ERROR_MESSAGE = "kasia.mimuwmap.extra.ERROR_MESSAGE";
    public static final String RESULT = "kasia.mimuwmap.extra.FIND_PROFESSOR_RESULT";
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_ERROR = 1;
    private ReceiverCallback receiverCallback;

    public FindProfessorResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiverCallback(ReceiverCallback receiver) {
        receiverCallback = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiverCallback != null) {
            if (resultCode == RESULT_CODE_OK) {
                receiverCallback.onSuccess((Professor) resultData.getSerializable(RESULT));
            } else if (resultCode == RESULT_CODE_ERROR) {
                receiverCallback.onError(resultData.getString(PARAM_ERROR_MESSAGE));

            }
        }
    }

    public interface ReceiverCallback {
        void onSuccess(Professor professor);

        void onError(String errorMessage);
    }
}
