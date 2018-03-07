/*
 * NavtexExample.java
 * Copyright (C) 2018 Raymond Dahlberg
 *
 * This file is part of Java Marine API.
 * <http://ktuukkan.github.io/marine-api/>
 *
 * Java Marine API is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Java Marine API is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java Marine API. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.marineapi.example;

import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.NRXSentence;
import net.sf.marineapi.nmea.sentence.TalkerId;
import net.sf.marineapi.nmea.util.DataStatus;
import net.sf.marineapi.nmea.util.Date;
import net.sf.marineapi.nmea.util.ReservedCharacters;
import net.sf.marineapi.nmea.util.Time;

import java.util.ArrayList;


/**
 * Demonstrates the usage of NRX - Navtex Received Message sentence
 * 
 * @author Raymond Dahlberg
 */
public class NavtexExample {

	public static void main(String[] args) {
		
		// Create a fresh NRX parser
		SentenceFactory sf = SentenceFactory.getInstance();
		NRXSentence nrx = (NRXSentence) sf.createParser(TalkerId.CR, "NRX");
		
		// should output "$CRNRX,,,,,,,,,,,,,*79"
		System.out.println(nrx.toSentence());
		
		String messageBody = new String(
				"210640 UTC FEB\n" +
						"ESTONIAN NAV WARN 006/18\n" +
						"GULF OF FINLAND\n" +
						"SINCE 01 MAR 18\n" +
						"VESSELS NEEDING ASSISTANCE OF ICEBREAKER IN THE P*RT OF SILLAME\n" +
						"HAVE TO COMPLY WITH THE FOLLOWING REQUIREMENTS:\n" +
						"- ICE CL*SS 1D LLOYD'S REGISTER OR ALTERNATIVE EQUIVALENT,\n" +
						"- MAIN ENGINE POWER AT LEAST 1200 KW.\n" +
						"NO ICEBREAKER SERVICE FOR TUGS AND BARGES");



		System.out.println("Original message length: " + messageBody.length());

		ReservedCharacters rc = new ReservedCharacters();
		String encodedMessage = rc.getEncodedText(messageBody);

		System.out.println("Encoded message length: " + encodedMessage.length());

		// Split message text so each sentence does not exceed the maximum length. The first sentence can have
		// less text since it need data in all fields, while the rest of the sentences can use 0 fields for data
		// already presented in the first sentence.
		ArrayList<String> messageChunks = new ArrayList<>();

		int maxTextLengthFirstSentence = 28;
		int maxTextLengthSentence = 57;
		int index = 0;
		int endIndex = maxTextLengthFirstSentence + 1;
		messageChunks.add(encodedMessage.substring(index, endIndex));
		index = endIndex;
		while (index < encodedMessage.length()) {
			endIndex = index + maxTextLengthSentence + 1;
			if (endIndex >= encodedMessage.length()) {
				endIndex = encodedMessage.length();
			}
			messageChunks.add(encodedMessage.substring(index, endIndex));
			index = endIndex;
		}

		System.out.println("\n\nMessage split into the following sentences: (" + messageChunks.size() + ")");
		for (String s : messageChunks) {
			System.out.println(s);
		}

		// Print message formatted as NRX sentences. Store message strings in list so we can test
		// parsing it back to the original message.
		System.out.println("\nNRX sentences for the whole message:\n");
		ArrayList<String> nrxMessages = new ArrayList<>();

		for (int i = 0; i < messageChunks.size(); i++) {
			nrx.reset();
			if (i == 0) {
				nrx.setMessageCode("UA98");
				nrx.setFrequencyTableIndex(1);
				nrx.setTime(new Time(10, 23, 8.0));
				nrx.setDate(new Date(2018, 3, 1));
				nrx.setTotalNumberOfCharactersInSeries(messageBody.length());
				nrx.setTotalNumberOfBadCharacters(0);
				nrx.setStatus(DataStatus.ACTIVE);
			}
			nrx.setNumberOfSentences(messageChunks.size());
			nrx.setSentenceNumber(i + 1);
			nrx.setSequentialId(0);
			nrx.setMessageBody(messageChunks.get(i));
			System.out.println(nrx.toSentence());
			nrxMessages.add(nrx.toSentence());
		}

		// Playing back the created sentences to compile the complete message back to the original
		StringBuilder nrxMessageText = new StringBuilder();
		for (String nrxMessage : nrxMessages) {
			NRXSentence nrxSentence = (NRXSentence) sf.createParser(nrxMessage);
			nrxMessageText.append(nrxSentence.getMessageBody());
		}

		System.out.println("\nMessage compiled from individual sentences: ");
		String compiledMessage = rc.getDecodedText(nrxMessageText.toString());
		System.out.println(compiledMessage);

		System.out.println("\nCompiled message matches original: " + ((compiledMessage.compareTo(messageBody) == 0) ? "OK" : "NOT OK"));
	}
}
