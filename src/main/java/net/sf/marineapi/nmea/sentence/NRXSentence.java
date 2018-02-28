/*
 * ZDASentence.java
 * Copyright (C) 2010 Kimmo Tuukkanen
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
package net.sf.marineapi.nmea.sentence;

import net.sf.marineapi.nmea.util.DataStatus;
import net.sf.marineapi.nmea.util.Time;

import java.util.Date;

/**
 * NAVTEX Received Message
 * <p>
 * Example: <br>
 * <code>$CRNRX,xxx,xxx,xx,aaxx,x,hhmmss.ss,xx,xx,xxxx,x.x,x.x,A,c--c*hh<CR><LF></code>
 *
 * @author Raymond Dahlberg
 */
public interface NRXSentence extends TimeSentence, DateSentence {

	/**
	 * Get total number of sentences required for the complete NAVTEX message. From 1 to 999.
	 *
	 * @return Total number of sentences for this NAVTEX message
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	int getNumberOfSentences();

	/**
	 * Get sentence number, the order this sentence is in the NAVTEX message. From 1 to 999.
	 *
	 * @return Sentence number
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	int getSentenceNumber();

	/**
	 * Get sequential message id. From 0 to 99.
	 *
	 * @return Sequential message id
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	int getSequentialId();

	/**
	 * Get the NAVTEX message code
	 *
	 * @return message code
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	String getMessageCode();

	/**
	 * Get frequency table index from 0 to 9.
	 * <ul>
	 *     <li>0 = not received over air (i.e. test messages)</li>
	 *     <li>1 = 490 kHz</li>
	 *     <li>2 = 518 kHz</li>
	 *     <li>3 = 4209.5 kHz</li>
	 *     <li>4 through 9 reserved for future use</li>
	 * </ul>
	 *
	 * @return Frequency table index
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	int getFrequencyTableIndex();

	/**
	 * Get total number of characters in this series of NRX sentences.
	 *
	 * @return total number of characters
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	int getTotalNumberOfCharactersInSeries();

	/**
	 * Get total number of bad characters.
	 *
	 * @return total number of bad characters
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	int getTotalNumberOfBadCharacters();

	/**
	 * Get the data quality status, valid or invalid.
	 *
	 * @return {@link DataStatus#ACTIVE} or {@link DataStatus#VOID}
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	DataStatus getStatus();

	/**
	 * Get message body
	 * (may not be the complete message, since it can be split over several sentences).
	 *
	 * @return the message body of this sentence.
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	String getMessageBody();

	/**
	 * Set total number of sentences required for the complete NAVTEX message. From 1 to 999.
	 *
	 * @param numberOfSentences from 1 to 999.
	 */
	void setNumberOfSentences(int numberOfSentences);

	/**
	 * Set sentence number, the order this sentence is in the NAVTEX message
	 *
	 * @param sentenceNumber from 1 to 999.
	 */
	void setSentenceNumber(int sentenceNumber);

	/**
	 * Set sequential message id.
	 *
	 * @param sequentialId from 0 to 99.
	 */
	void setSequentialId(int sequentialId);

	/**
	 * Set NAVTEX message code
	 * @param messageCode string composed of transmitter coverage area, type of message and serial number.
	 */
	void setMessageCode(String messageCode);

	/**
	 * Set frequency table index from 0 to 9.
	 * <ul>
	 *     <li>0 = not received over air (i.e. test messages)</li>
	 *     <li>1 = 490 kHz</li>
	 *     <li>2 = 518 kHz</li>
	 *     <li>3 = 4209.5 kHz</li>
	 *     <li>4 through 9 reserved for future use</li>
	 * </ul>
	 * @param frequencyTableIndex 0, 1 or 2
	 */
	void setFrequencyTableIndex(int frequencyTableIndex);

	/**
	 * Set total number of characters in this series of NRX sentences.
	 * @param totalNumberOfCharactersInSeries total number of characters of message body
	 */
	void setTotalNumberOfCharactersInSeries(int totalNumberOfCharactersInSeries);

	/**
	 * Set total number of characters in this series of NRX sentences.
	 * @param totalNumberOfBadCharacters number of bad characters
	 */
	void setTotalNumberOfBadCharacters(int totalNumberOfBadCharacters);

	/**
	 * Set the data quality status, valid or invalid.
	 *
	 * @param status DataStatus to set
	 * @throws net.sf.marineapi.nmea.parser.DataNotAvailableException If the data is
	 *             not available.
	 * @throws net.sf.marineapi.nmea.parser.ParseException If the field contains
	 *             unexpected or illegal value.
	 */
	void setStatus(DataStatus status);

	/**
	 * Set message body of this sentence
	 * (may not be the complete message, since it can be split over several sentences).
	 * @param messageBody of this sentence
	 */
	void setMessageBody(String messageBody);
}
