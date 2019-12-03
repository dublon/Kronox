// Ian's modifications to MAHs KronoxGUI. Includes extended character support (Dansk/Norsk, Fransk, Polsk) and has hard coded room information pertaining specifically to Alnarpgsården. 
// Deployment machine requires a 1080p monitor in portrait mode and a Java RE.
// 20171025 Ian Dublon ian.dublon@slu.se

package publicScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class KronoxParser {
	private PublicScreenGUI gui;
	private Timer t;

	public KronoxParser(PublicScreenGUI gui) {
		this.gui = gui;
		t = new Timer();
		t.schedule(new ParseKronox(), 0, Constants.SECONDS_BETWEEN_KRONOXPARSING * 1000);
	}

	public void parseFromKronox() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(Constants.kronoxStringAgTodayAndTomorrowMedForklaringar);
			
//			Document doc2 = builder.parse(Constants.yrAlnarp);
//			NodeList forecast = doc2.getElementsByTagName("forecast");
//			for (int l = 0; l < forecast.getLength(); l++) {
//			}
			
			
			NodeList bookings = doc.getElementsByTagName("schemaPost");
			for (int i = 0; i < bookings.getLength(); i++) {
				SchemaPost thisBooking = new SchemaPost();
				Node p = bookings.item(i);
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element doc1 = (Element) p;
					NodeList items2 = doc1.getElementsByTagName("bokadeDatum");
					for (int t = 0; t < items2.getLength(); t++) {
						Node n1 = items2.item(t);
						if (n1.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) n1;
							// Parse Start and Enddates
							SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss"); // first example
							String startDateTime = eElement.getAttribute("startDatumTid");
							Date d1 = format1.parse("1961-04-29 23:14:20");
							// format1.get
							try {
								d1 = format1.parse(startDateTime);
							} catch (Exception e) {
							}
							Calendar startTimeCal = Calendar.getInstance();
							startTimeCal.setTime(d1);
							String endDateTime = eElement.getAttribute("slutDatumTid");
							Date d2 = format1.parse("1961-04-29 23:14:20");
							try {
								d2 = format1.parse(endDateTime);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Calendar endTimeCal = Calendar.getInstance();
							endTimeCal.setTime(d2);
							thisBooking.setStartTime(startTimeCal);
							thisBooking.setEndTime(endTimeCal);

						}
					}
				}
				// Who and where
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element booking = (Element) p;
					NodeList resource = booking.getElementsByTagName("resursNod");
					for (int j = 0; j < resource.getLength(); j++) {
						Node n = resource.item(j);
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							Element classType = (Element) n;
							String id = classType.getAttribute("resursTypId");
							NodeList classResource = classType.getElementsByTagName("resursId");
							for (int k = 0; k < classResource.getLength(); k++) {
								Node specifiedClass = classResource.item(k);
								if (specifiedClass.getNodeType() == Node.ELEMENT_NODE) {
									Element name = (Element) specifiedClass;
									if (id.contains("UTB_KURSINSTANS_GRUPPER")) {
										thisBooking.addCourse(name.getTextContent());
									}
									if (id.contains("RESURSER_LOKALER")) {
										thisBooking.addRoom(RumKodDecode(name.getTextContent()));
									}
									if (id.contains("RESURSER_SIGNATURER")) {
										thisBooking.addTeacher(name.getTextContent());
									}
								}
							}
						}
					}
					// Add if there are UTB_KURSINSTANS_GRUPPER else it is grupproom
					if (thisBooking.getCourses().size() > 0) {
						// Is it only bookings that are within limit
						Calendar now = Calendar.getInstance();
						Calendar addEvenIfLate = Calendar.getInstance();
						addEvenIfLate.setTime(thisBooking.getStartTime().getTime());
						addEvenIfLate.add(Calendar.MINUTE, Constants.minutesLate);
						// c.add(Calendar.MINUTE,Constants.minutesBefore);
						if (thisBooking.getStartTime().after(now)) { // StartTimeless than Constants.minutesbefore
							if ((thisBooking.getStartTime().get(Calendar.DAY_OF_YEAR)
									- Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == 1)) {
								// Tomorrow and list not full
								if (Constants.bookingsList.size() < Constants.maxNumberPosts) { // Fill it up
									Constants.bookingsList.add(thisBooking);
								} else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 18) {
									Constants.bookingsList.add(thisBooking); // Ligga till alla i morgon om det är kväll
								}
							} else if ((thisBooking.getStartTime().get(Calendar.DAY_OF_YEAR)
									- Calendar.getInstance().get(Calendar.DAY_OF_YEAR) > 1)) {
								// After tomorrow
								if (Constants.bookingsList.size() < Constants.maxNumberPosts) { // Fill it up
									Constants.bookingsList.add(thisBooking);
								}
							} else {
								if (Constants.bookingsList.size() < Constants.maxNumberPosts) {
									Constants.bookingsList.add(thisBooking);
								}
							}
						} else {
							if (thisBooking.getEndTime().after(now)) { // Has not finished yet
								if (now.before(addEvenIfLate)) { // And only Constants.minutesLate after start
									if (Constants.addOngoingCourses) {
										Constants.bookingsList.add(thisBooking);
									}
								}
							}
						}

					}
				}
			}

			// ResurseMapping
			NodeList forklaringsraders = doc.getElementsByTagName("forklaringsrader");
			for (int i = 0; i < forklaringsraders.getLength(); i++) {
				Node p = forklaringsraders.item(i);
				if (p.getNodeType() == Node.ELEMENT_NODE) {
					Element doc1 = (Element) p;
					// System.out.println("typ: "+doc1.getAttribute("typ"));
					if (doc1.getAttribute("typ").equals("RESURSER_SIGNATURER")) {
						NodeList rad = doc1.getElementsByTagName("rad");
						for (int j = 0; j < rad.getLength(); j++) {
							String firstName = "";
							String lastName = "";
							String id = "";
							Node n1 = rad.item(j);
							Element e2 = (Element) n1;
							NodeList kolumn = e2.getElementsByTagName("kolumn");
							for (int k = 0; k < kolumn.getLength(); k++) {

								Element e3 = (Element) kolumn.item(k);
								if (e3.getAttribute("rubrik").equals("Id")) {
									id = e3.getTextContent().toLowerCase();
								}
								if (e3.getAttribute("rubrik").equals("ForNamn")) {
									firstName = e3.getTextContent();
								}
								if (e3.getAttribute("rubrik").equals("EfterNamn")) {
									lastName = e3.getTextContent();
								}
							}
							Constants.resurser_signaturer.put(id, htmlDecode(firstName) + " " + htmlDecode(lastName));
						}
					}
					if (doc1.getAttribute("typ").equals("UTB_KURSINSTANS_GRUPPER")) {
						NodeList rad = doc1.getElementsByTagName("rad");
						for (int j = 0; j < rad.getLength(); j++) {
							String kursNamn = "";
							String id = "";
							Node n1 = rad.item(j);
							Element e2 = (Element) n1;
							NodeList kolumn = e2.getElementsByTagName("kolumn");
							for (int k = 0; k < kolumn.getLength(); k++) {

								Element e3 = (Element) kolumn.item(k);
								if (e3.getAttribute("rubrik").equals("Id")) {
									// System.out.println("Id= "+e3.getTextContent());
									id = e3.getTextContent();
								}
								if (e3.getAttribute("rubrik").equals("KursNamn_SV")) {
									// System.out.println("ForNamn= "+e3.getTextContent());
									kursNamn = e3.getTextContent();
								}
							}
							Constants.utb_kursinstans_grupper.put(id, htmlDecode(kursNamn));
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(Constants.bookingsList);

	}

	private String htmlDecode(String text) {
		text = text.replace("&#128;", "€");
		text = text.replace("&#131;", "ƒ");
		text = text.replace("&#134;", "†");
		text = text.replace("&#135;", "‡");
		text = text.replace("&#137;", "‰");
		text = text.replace("&#138;", "Š");
		text = text.replace("&#140;", "Œ");
		text = text.replace("&#142;", "Ž");
		text = text.replace("&#153;", "™");
		text = text.replace("&#154;", "š");
		text = text.replace("&#156;", "œ");
		text = text.replace("&#158;", " ž");
		text = text.replace("&#159;", "Ÿ");
		text = text.replace("&#162;", "¢");
		text = text.replace("&#163;", "£");
		text = text.replace("&#164;", "¤");
		text = text.replace("&#165;", " ¥");
		text = text.replace("&#166;", "¦");
		text = text.replace("&#167;", "§");
		text = text.replace("&#169;", "©");
		text = text.replace("&#170;", "ª");
		text = text.replace("&#174;", "®");
		text = text.replace("&#175;", "¯");
		text = text.replace("&#176;", " °");
		text = text.replace("&#177;", "±");
		text = text.replace("&#178;", "²");
		text = text.replace("&#179;", "³");
		text = text.replace("&#181;", "µ");
		text = text.replace("&#182;", "¶");
		text = text.replace("&#185;", "¹");
		text = text.replace("&#186;", "º");
		text = text.replace("&#188;", "¼");
		text = text.replace("&#189;", "½");
		text = text.replace("&#190;", "¾");
		text = text.replace("&#191;", "¿");
		text = text.replace("&#192;", "À");
		text = text.replace("&#193;", "Á");
		text = text.replace("&#194;", "Â");
		text = text.replace("&#195;", "Ã");
		text = text.replace("&#196;", "Ä");
		text = text.replace("&#197;", "Å");
		text = text.replace("&#198;", "Æ");
		text = text.replace("&#199;", "Ç");
		text = text.replace("&#200;", "È");
		text = text.replace("&#201;", "É");
		text = text.replace("&#202;", "Ê");
		text = text.replace("&#203;", "Ë");
		text = text.replace("&#204;", "Ì");
		text = text.replace("&#205;", "Í");
		text = text.replace("&#206;", "Î");
		text = text.replace("&#207;", "Ï");
		text = text.replace("&#208;", "Ð");
		text = text.replace("&#209;", "Ñ");
		text = text.replace("&#210;", "Ò");
		text = text.replace("&#211;", "Ó");
		text = text.replace("&#212;", "Ô");
		text = text.replace("&#213;", "Õ");
		text = text.replace("&#214;", "Ö");
		text = text.replace("&#215;", "×");
		text = text.replace("&#216;", "Ø");
		text = text.replace("&#217;", "Ù");
		text = text.replace("&#218;", "Ú");
		text = text.replace("&#219;", "Û");
		text = text.replace("&#220;", "Ü");
		text = text.replace("&#221;", "Ý");
		text = text.replace("&#222;", "Þ");
		text = text.replace("&#223;", "ß");
		text = text.replace("&#224;", "à");
		text = text.replace("&#225;", "á");
		text = text.replace("&#226;", "â");
		text = text.replace("&#227;", "ã");
		text = text.replace("&#228;", "ä");
		text = text.replace("&#229;", "å");
		text = text.replace("&#230;", "æ");
		text = text.replace("&#231;", "ç");
		text = text.replace("&#232;", "è");
		text = text.replace("&#233;", "é");
		text = text.replace("&#234;", "ê");
		text = text.replace("&#235;", "ë");
		text = text.replace("&#236;", "ì");
		text = text.replace("&#237;", "í");
		text = text.replace("&#238;", "î");
		text = text.replace("&#239;", "ï");
		text = text.replace("&#240;", "ð");
		text = text.replace("&#241;", "ñ");
		text = text.replace("&#242;", "ò");
		text = text.replace("&#243;", "ó");
		text = text.replace("&#244;", "ô");
		text = text.replace("&#245;", "õ");
		text = text.replace("&#246;", "ö");
		text = text.replace("&#247;", "÷");
		text = text.replace("&#248;", "ø");
		text = text.replace("&#249;", "ù");
		text = text.replace("&#250;", "ú");
		text = text.replace("&#251;", "û");
		text = text.replace("&#252;", "ü");
		text = text.replace("&#253;", "ý");
		text = text.replace("&#260;", "Ą");
		text = text.replace("&#261;", "ą");
		text = text.replace("&#262;", "Ć");
		text = text.replace("&#263;", "ć");
		text = text.replace("&#280;", "Ę");
		text = text.replace("&#281;", "ę");
		text = text.replace("&#321;", "Ł");
		text = text.replace("&#322;", "ł");

		return text;
	}

	private String RumKodDecode(String text) {
		text = text.replace("M-621120", "Sal 120");
		text = text.replace("M-621112", "Sal 112");
		text = text.replace("M-621102", "Sal 102");
		text = text.replace("M-620212", "Videokonferensstudio");
		text = text.replace("M-621118", "Träverkstad");
		text = text.replace("M-621121C", "Studio C");
		text = text.replace("M-621121B", "Studio B");
		text = text.replace("M-621121A", "Studio A");
		text = text.replace("M-620128", "Stallet (datorsal)");
		text = text.replace("M-620107", "Sal 107");
		text = text.replace("M-620106", "Sal 106");
		text = text.replace("M-62130B", "Olympen B");
		text = text.replace("M-62130A", "Olympen A");
		text = text.replace("M-62120C", "Magasinet C");
		text = text.replace("M-62120B", "Magasinet B");
		text = text.replace("M-62120A", "Magasinet A");
		text = text.replace("M-620202A", "Loftet undervisning");
		text = text.replace("M-620202", "Loftet");
		text = text.replace("M-620130", "Ladan");
		text = text.replace("M-621210B", "Hyllan B");
		text = text.replace("M-621210A", "Hyllan A");
		text = text.replace("M-620226", "Grupprum Rosa");
		text = text.replace("M-620225", "Grupprum Primula");
		text = text.replace("M-620224", "Grupprum Daphne");
		text = text.replace("M-620224A", "Grupprum Betula");
		text = text.replace("M-620222", "Grupprum Aralia");
		text = text.replace("M-620223", "Grupprum Adonis");
		text = text.replace("M-621115", "Gips");
		text = text.replace("M-620102", "Foajén");
		text = text.replace("M-620148", "Aulan");
		text = text.replace("M-621114", "Ateljé");
		text = text.replace("M-621208B", "Arken B");
		text = text.replace("M-621208A", "Arken A");
		text = text.replace("M-621207", "Akvariet (datorsal)");
		return text;
	}

	private class ParseKronox extends TimerTask {
		@Override
		public void run() {
			// System.out.println("PARSING");
			Constants.bookingsList.clear();
			parseFromKronox();
			gui.updateInfo();
		}
	}

}
