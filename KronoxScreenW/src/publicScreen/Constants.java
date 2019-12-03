package publicScreen;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;

public class Constants {
	// Here is were you decide what to show.
	public static final String kronoxStringAgToday = "http://kronox-rapport.slu.se/setup/jsp/SchemaXML.jsp?startDatum=idag&intervallTyp=d&intervallAntal=1&sokMedAND=false&sprak=SV&resurser=l.M-620102%2Cl.M-620106%2Cl.M-620107%2Cl.M-620128%2Cl.M-620130%2Cl.M-620148%2Cl.M-620202%2Cl.M-620202A%2Cl.M-620212%2Cl.M-620222%2Cl.M-620223%2Cl.M-620224%2Cl.M-620224A%2Cl.M-620225%2Cl.M-620226%2Cl.M-621102%2Cl.M-621112%2Cl.M-621114%2Cl.M-621115%2Cl.M-621118%2Cl.M-621120%2Cl.M-621121A%2Cl.M-621121B%2Cl.M-621121C%2Cl.M-621207%2Cl.M-621208A%2Cl.M-621208B%2Cl.M-62120A%2Cl.M-62120B%2Cl.M-62120C%2Cl.M-621210A%2Cl.M-621210B%2Cl.M-62130A%2Cl.M-62130B%2C";
	public static final String kronoxStringAgTodayAndTomorrow = "http://kronox-rapport.slu.se/setup/jsp/SchemaXML.jsp?startDatum=idag&intervallTyp=d&intervallAntal=2&sokMedAND=false&sprak=SV&resurser=l.M-620102%2Cl.M-620106%2Cl.M-620107%2Cl.M-620128%2Cl.M-620130%2Cl.M-620148%2Cl.M-620202%2Cl.M-620202A%2Cl.M-620212%2Cl.M-620222%2Cl.M-620223%2Cl.M-620224%2Cl.M-620224A%2Cl.M-620225%2Cl.M-620226%2Cl.M-621102%2Cl.M-621112%2Cl.M-621114%2Cl.M-621115%2Cl.M-621118%2Cl.M-621120%2Cl.M-621121A%2Cl.M-621121B%2Cl.M-621121C%2Cl.M-621207%2Cl.M-621208A%2Cl.M-621208B%2Cl.M-62120A%2Cl.M-62120B%2Cl.M-62120C%2Cl.M-621210A%2Cl.M-621210B%2Cl.M-62130A%2Cl.M-62130B%2C";
	public static final String kronoxStringAgTodayMedForklaringar = "http://kronox-rapport.slu.se/setup/jsp/SchemaXML.jsp?startDatum=idag&intervallTyp=d&intervallAntal=2&forklaringar=true&sokMedAND=false&sprak=SV&resurser=l.M-620102%2Cl.M-620106%2Cl.M-620107%2Cl.M-620128%2Cl.M-620130%2Cl.M-620148%2Cl.M-620202%2Cl.M-620202A%2Cl.M-620212%2Cl.M-620222%2Cl.M-620223%2Cl.M-620224%2Cl.M-620224A%2Cl.M-620225%2Cl.M-620226%2Cl.M-621102%2Cl.M-621112%2Cl.M-621114%2Cl.M-621115%2Cl.M-621118%2Cl.M-621120%2Cl.M-621121A%2Cl.M-621121B%2Cl.M-621121C%2Cl.M-621207%2Cl.M-621208A%2Cl.M-621208B%2Cl.M-62120A%2Cl.M-62120B%2Cl.M-62120C%2Cl.M-621210A%2Cl.M-621210B%2Cl.M-62130A%2Cl.M-62130B%2C";
	public static final String kronoxStringAgTodayAndTomorrowMedForklaringar = "http://kronox-rapport.slu.se/setup/jsp/SchemaXML.jsp?startDatum=idag&intervallTyp=d&intervallAntal=4&forklaringar=true&sokMedAND=false&sprak=SV&resurser=l.M-620102%2Cl.M-620106%2Cl.M-620107%2Cl.M-620128%2Cl.M-620130%2Cl.M-620148%2Cl.M-620202%2Cl.M-620202A%2Cl.M-620212%2Cl.M-620222%2Cl.M-620223%2Cl.M-620224%2Cl.M-620224A%2Cl.M-620225%2Cl.M-620226%2Cl.M-621102%2Cl.M-621112%2Cl.M-621114%2Cl.M-621115%2Cl.M-621118%2Cl.M-621120%2Cl.M-621121A%2Cl.M-621121B%2Cl.M-621121C%2Cl.M-621207%2Cl.M-621208A%2Cl.M-621208B%2Cl.M-62120A%2Cl.M-62120B%2Cl.M-62120C%2Cl.M-621210A%2Cl.M-621210B%2Cl.M-62130A%2Cl.M-62130B%2C";
	public static final String yrAlnarp = "https://www.yr.no/place/Sweden/Scania/Alnarp/forecast.xml";
	public static final int SECONDS_BETWEEN_KRONOXPARSING = 180;
	//

	public static HashMap<String, String> resurser_signaturer = new HashMap<String, String>();
	public static HashMap<String, String> utb_kursinstans_grupper = new HashMap<String, String>();
	public static ArrayList<SchemaPost> bookingsList = new ArrayList<SchemaPost>();
	public static int minutesBefore = 40; // Show these as green minutes before they start
	public static int minutesLate = 20; // Let the bookings be there for 20 more minutes after start for late arrives
										// (Not Me) Show as Yellow
	public static int maxNumberPosts = 24;
	public static boolean addOngoingCourses = true;
}
