/*
 * TITLE: Program 7.1a
 *
 * @(#)Path 2002/07/21
 * @author Charles W. Kann III
 *
 * Copyright (c) 2002 CRC Press
 * All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this
 * copyright notice appears in all copies.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package elevator.animator;

import java.awt.Point;

/**
 *  Purpose:    The Path interface allows a user to define a path that
 *              object should travel through, and then simply call the
 *              hasMoreSteps and nextPosition methods to walk through 
 *              the path.
 *		
 *		Note that in the Path could be a simple straight line, 
 *		a spline curve, a Bessel function, a Path with multiple
 *              segments, etc.
 */

public interface Path {

    /** 
     *  Check to see if the path has MoreSteps
     */
    public boolean hasMoreSteps();

    /** 
     *  Get the next position.  If the path has no more steps, return
     *  the current position.
     */
    public Point nextPosition();
}

