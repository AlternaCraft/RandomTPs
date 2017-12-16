/*
 * Copyright (C) 2017 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alternacraft.randomtps.API.Errors;

import com.alternacraft.aclib.exceptions.ErrorExecutor;
import com.alternacraft.aclib.exceptions.ErrorFormat;

/**
 *
 * @author AlternaCraft
 */
public enum DBErrors implements ErrorFormat {

    NULL("There was an error with the database", new int[]{0, 0},
            (msg, data, c_error) -> msg.contains("null"));

    private final String error_str;
    private final int[] error_code;
    private final ErrorExecutor error_exe;

    private DBErrors(String msg, int[] error_code, ErrorExecutor exe) {
        this.error_str = msg;
        this.error_code = error_code;
        this.error_exe = exe;
    }

    @Override
    public String getMessage() {
        return this.error_str;
    }

    @Override
    public int[] getErrorCode() {
        return this.error_code;
    }

    @Override
    public ErrorExecutor getErrorExecutor() {
        return this.error_exe;
    }
}
