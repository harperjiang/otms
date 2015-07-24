package org.harper.otms.common.dwr;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.directwebremoting.convert.BaseV20Converter;
import org.directwebremoting.dwrp.ProtocolConstants;
import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.extend.NonNestedOutboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

public class DateConverter extends BaseV20Converter implements Converter {

	public Object convertInbound(Class<?> paramType, InboundVariable data,
			InboundContext inctx) throws MarshallException {
		String value = data.getValue();

		// If the text is null then the whole bean is null
		if (value.trim().equals(ProtocolConstants.INBOUND_NULL)) {
			return null;
		}

		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date date = format.parse(value);
			if (paramType == Date.class) {
				return date;
			} else if (paramType == java.sql.Date.class) {
				return new java.sql.Date(date.getTime());
			} else if (paramType == Time.class) {
				return new Time(date.getTime());
			} else if (paramType == Timestamp.class) {
				return new Timestamp(date.getTime());
			} else if (paramType == Calendar.class) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				return cal;
			} else {
				throw new MarshallException(paramType);
			}
		} catch (MarshallException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new MarshallException(paramType, ex);
		}
	}

	public OutboundVariable convertOutbound(Object data, OutboundContext outctx)
			throws MarshallException {

		Calendar cal = null;
		if (data instanceof Calendar) {
			cal = (Calendar) data;
		} else if (data instanceof Date) {
			cal = Calendar.getInstance();
			cal.setTime((Date) data);
		} else {
			throw new MarshallException(data.getClass());
		}
		String constructor = MessageFormat.format(
				"new Date({0},{1},{2},{3},{4},{5})",
				Integer.toString(cal.get(Calendar.YEAR)),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));
		return new NonNestedOutboundVariable(constructor);
	}
}
