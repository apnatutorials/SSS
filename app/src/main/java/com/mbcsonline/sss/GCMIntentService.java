package com.mbcsonline.sss;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	private Controller aController = null;

	public GCMIntentService() {
		// Call extended class Constructor GCMBaseIntentService
		super(Config.GOOGLE_SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {

		// Get Global Controller Class object (see application tag in
		// AndroidManifest.xml)
		if (aController == null)
			aController = new Controller(this);

		Log.i(TAG, "Device registered: regId = " + registrationId);
		aController.displayMessageOnScreen(context,
				"Your device registred with Sigma News");
		// Log.d("NAME", MainActivity.name);
		aController.register(context, "", "", registrationId);
	}

	/**
	 * Method called on device unregistred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if (aController == null)
			aController = new Controller(this);
		Log.i(TAG, "Device unregistered");
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_unregistered));
		aController.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message from GCM server
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {

		if (aController == null)
			aController = new Controller(this);

		Log.i(TAG, "Received message" + intent.getExtras());

		if (intent.getExtras() != null) {
			String message = intent.getExtras().getString("price");

			aController.displayMessageOnScreen(context, message);
			// notifies user
			// generateNotification(context, message);//vivek
			sendNotification(context, message);
		}
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {

		if (aController == null)
			aController = new Controller(this);

		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		aController.displayMessageOnScreen(context, message);
		// notifies user
		// generateNotification(context, message);
		sendNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {

		if (aController == null)
			aController = new Controller(this);

		Log.i(TAG, "Received error: " + errorId);
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {

		if (aController == null)
			aController = new Controller(this);

		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Create a notification to inform the user that server has sent a message.
	 */
	private static void _generateNotification(Context context, String message) {
		int icon = R.mipmap.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		//Notification notification = new Notification(icon, message, when);

		NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
		//NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


		// Vivek:we can add different type notifications like below:but
		// available in api 16
		// Notification notification = new Notification.BigTextStyle(
		// new Notification.Builder(context)
		// // .setContentTitle("New mail from " + sender.toString())
		// .setContentText(message)
		// .setSmallIcon(R.drawable.ic_launcher)
		// // .setLargeIcon(aBitmap)
		// )
		// .bigText(message)
		// .build();

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, LoginActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);



//		nc.setDefaults(Notification.DEFAULT_ALL);
//		nc.setDeleteIntent(intent);
//		nc.setContentText(message);
//		nc.setWhen(when);
//		nc.setSmallIcon()
		nc.setContentTitle(title)
				.setContentText(message)
				.setTicker(message)
				.setWhen(when)
				.setDeleteIntent(intent)
				.setSmallIcon(icon);

		notificationManager.notify(0, nc.build());


//		notification.setLatestEventInfo(context, title, message, intent);
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
//		nc.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
//		notification.defaults |= Notification.DEFAULT_VIBRATE;
//		notificationManager.notify(0, notification);

	}

	public static final int NOTIFICATION_ID = 1;

	private void sendNotification(Context context, String msg) {
		NotificationManager mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new
		// Intent(this, MainActivity.class), 0);
		Intent notificationIntent = new Intent(context, LoginActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("GIZO")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg);

		mBuilder.setContentIntent(contentIntent);

		Notification notification = mBuilder.build();
		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager.notify(msg, NOTIFICATION_ID, notification);
	}
}
