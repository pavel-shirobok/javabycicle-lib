/*
 * Copyright (C) 2011 Shirobok Pavel
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Contact information:
 * email: ramshteks@gmail.com
 */

package ramshteks.java.utils;

import ramshteks.java.bytes.BytesArray;
import ramshteks.java.bytes.IBytesArray;

public class ByteUtils {

	public static String getUTFString(byte[] bytes){
		String result;

		try{
			result = new String(bytes, "UTF-8");
		}catch (Exception e){
			return "Excepted string!";
		}

		return result;
	}

    public  static byte[] getUTFStringBytes(String string){
        byte[] say_bytes = null;
        try{
			say_bytes = string.getBytes("UTF-8");
		}catch (Exception e){}
        return say_bytes;
    }

	public static IBytesArray getBytesArray(){
		return new BytesArray();
	}

	public static byte[] getShortBytes(short value){
		byte[] result = new byte[2];
		result[0] = (byte)((value>>8)&0xFF);
		result[1] = (byte)((value)&0xFF);
		return  result;
	}

	public static byte[] getIntBytes(int value){
		byte[] result = new byte[4];
		result[0] = (byte)(value>>24);
		result[1] = (byte)((value>>16)&0xff);
		result[2] = (byte)((value>>8)&0xff);
		result[3] = (byte)((value)&0xff);
		return  result;
	}

	public static byte[] getLongBytes(long value){
		byte[] result = new byte[8];
		for(int i=0; i<8; i++){
			result[i] = (byte)((value>>((long)(Math.pow((double )2,(double)8) - (i+1)*8)))&0xff);
		}
		return result;
	}

	public static short getShort(byte[] bytes) {
		return getShort(bytes, 0);
	}

	public static short getShort(byte[] bytes, int offset) {
		if (offset + 2 < bytes.length) {
			return (short) ((int) bytes[offset] << 8 | bytes[offset + 1]);
		}
		return Short.MIN_VALUE;
	}

	public static byte[] getSubArray(byte[] bytes, int start) {
		if (bytes == null || start < 0 || start >= bytes.length) return null;

		int new_len = bytes.length - start;
		byte[] result = new byte[new_len];

		System.arraycopy(bytes, 2, result, 0, new_len);

		return result;
	}
}
