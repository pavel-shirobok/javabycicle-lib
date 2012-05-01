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

package ramshteks.java.geom;

public class Coordinate
{
	private int _x;
	private int _y;

	public Coordinate(int x, int y)
	{
		_y = y;
		_x = x;
	}
	/*
	public function setPair(x:Number, y:Number):void {
		this.x = x;
		this.y = y;
	} */

	public int x()
	{
		return _x;
	}

	public int y()
	{
		return _y;
	}

	public void x(int value)
	{
		_x = value;
	}

	public void y(int value)
	{
		_y = value;
	}

	public String toString()
	{
		return "[Coordinate x=" + _x + " y=" + _y + "]";
	}

}



