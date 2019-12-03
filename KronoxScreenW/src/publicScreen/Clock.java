package publicScreen;

import java.util.Calendar;
import java.util.Date;

public class Clock {
	private PublicScreenGUI publicScreenGUI;
	Calendar calendar = Calendar.getInstance();
	int day = calendar.get(Calendar.DAY_OF_MONTH);
	int year = calendar.get(Calendar.YEAR);
	int minutes;
	int hours;
	String month = "-";

	public Clock(PublicScreenGUI publicScreenGUI) {
		this.publicScreenGUI = publicScreenGUI;
		new ClockThread().start();
	}

	private class ClockThread extends Thread {

		@Override
		public void run() {
			while (true) {
				calendar = Calendar.getInstance();
				minutes = calendar.get(Calendar.MINUTE);
				hours = calendar.get(Calendar.HOUR_OF_DAY);

				// Corrects time display by adding a 0 if each time variable is less than 10
				String hourCorrection = "";
				String minuteCorrection = "";

				if (hours < 10)
					hourCorrection = "0";
				if (minutes < 10)
					minuteCorrection = "0";

				String currentTime = hourCorrection + hours + ":" + minuteCorrection + minutes;

				switch (calendar.get(Calendar.MONTH)) {
				case 0:
					month = "Januari";
					break;
				case 1:
					month = "Februari";
					break;
				case 2:
					month = "Mars";
					break;
				case 3:
					month = "April";
					break;
				case 4:
					month = "Maj";
					break;
				case 5:
					month = "Jun";
					break;
				case 6:
					month = "Juli";
					break;
				case 7:
					month = "Augusti";
					break;
				case 8:
					month = "September";
					break;
				case 9:
					month = "Oktober";
					break;
				case 10:
					month = "November";
					break;
				case 11:
					month = "December";
					break;
				}
				day = calendar.get(Calendar.DAY_OF_MONTH);
				year = calendar.get(Calendar.YEAR);

				String currentDate = day + " " + month + " " + year;

				publicScreenGUI.setTimeOnLabel(currentTime, currentDate);

				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}
}