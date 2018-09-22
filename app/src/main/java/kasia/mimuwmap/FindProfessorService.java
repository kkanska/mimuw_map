package kasia.mimuwmap;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.io.IOException;

/**
 * An IntentService subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class FindProfessorService extends IntentService {
    private static final String ACTION_FIND_PROFESSOR = "kasia.mimuwmap.action.FIND_PROFESSOR";
    private static final String RESULT_RECEIVER = "kasia.mimuwmap.extra.RESULT_RECEIVER";
    private static final String QUERY = "kasia.mimuwmap.extra.QUERY";

    public FindProfessorService() {
        super("FindProfessorService");
    }

    /**
     * Starts this service to perform action FindProfessor with the given parameters. If
     * the service is already performing a task this action will be queued.
     */
    public static void startActionFindProfessor(Context context, String query,
                                                FindProfessorResultReceiver.ReceiverCallback receiverCallback) {
        FindProfessorResultReceiver resultReceiver = new FindProfessorResultReceiver(
                new Handler(context.getMainLooper()));
        resultReceiver.setReceiverCallback(receiverCallback);

        Intent intent = new Intent(context, FindProfessorService.class);
        intent.setAction(ACTION_FIND_PROFESSOR);
        intent.putExtra(QUERY, query);
        intent.putExtra(RESULT_RECEIVER, resultReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_FIND_PROFESSOR.equals(action)) {
                String query = intent.getStringExtra(QUERY);
                ResultReceiver resultReceiver = intent.getParcelableExtra(RESULT_RECEIVER);

                startForeground(1, getFindProfessorNotification(query));
                try {
                    handleActionFindProfessor(query, resultReceiver);
                } finally {
                    stopForeground(true);
                }
            }
        }
    }

    private Notification getFindProfessorNotification(String query) {
        Notification.Builder builder = new Notification.Builder(getBaseContext())
                .setSmallIcon(R.drawable.ic_search)
                .setTicker(getString(R.string.add_prof_notif_ticker))
                .setContentTitle(getString(R.string.add_prof_notif_title))
                .setContentText(getString(R.string.add_prof_notif_text, query))
                .setProgress(0, 0, true);
        return builder.build();
    }

    /**
     * Handle action FindProfessor in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFindProfessor(String query, ResultReceiver resultReceiver) {
        Bundle bundle = new Bundle();
        int code;
        Professor professor;
        try {
            professor = ProfessorFinder.scrapeProfessor(query);
            if (professor != null) {
                code = FindProfessorResultReceiver.RESULT_CODE_OK;
                bundle.putSerializable(FindProfessorResultReceiver.RESULT, professor);
            } else {
                code = FindProfessorResultReceiver.RESULT_CODE_ERROR;
                bundle.putSerializable(FindProfessorResultReceiver.PARAM_ERROR_MESSAGE,
                        getString(R.string.add_prof_search_failure_message, query));
            }
        } catch (IOException e) {
            code = FindProfessorResultReceiver.RESULT_CODE_ERROR;
            bundle.putSerializable(FindProfessorResultReceiver.PARAM_ERROR_MESSAGE,
                    getString(R.string.add_prof_internet_failure_message));
        }

        if (resultReceiver != null) {
            resultReceiver.send(code, bundle);
        }
    }
}
