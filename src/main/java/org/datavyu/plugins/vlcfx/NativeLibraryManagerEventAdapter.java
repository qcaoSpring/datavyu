package org.datavyu.plugins.vlcfx;

/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

/**
 * Default implementation of a native library manager event listener.
 * <p>
 * Simply override the methods you're interested in.
 */
public class NativeLibraryManagerEventAdapter implements NativeLibraryManagerEventListener {

    @Override
    public void start(String installTo, int installCount) {
    }

    @Override
    public void install(int number, String name) {
    }

    @Override
    public void end() {
    }

    @Override
    public void purge(String installTo) {
    }

    @Override
    public void purged(boolean result) {
    }
}