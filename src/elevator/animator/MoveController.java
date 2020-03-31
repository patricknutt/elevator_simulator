/*
 * TITLE: Program 10.4b
 *
 * @(#)ConcurrentBall 2002/07/21
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

/*
 *  Purpose:    This class implements the MoveController using
 *		composition.  Any class wishing to use this
 *		object includes it as a variable in that class.
 *
 *        	Note that this design allows the MoveController
 *		to be completely encapsulated, and so no using
 *		class can directly access any internal variables,
 *		and using classes do not need to implement parts
 *		of the logic for this class.  They only need to
 *		call the correct methods.
 *
 * 		Note that this MoveController class looks very
 *		much as if it implements a Path interface.  It
 *		does not since it is not a type of a Path, but 
 *		it could do so to make it easier to refit programs
 *		that use a Path variable and need to move to 
 *		a MoveController in a concurrent environment.
 *
 *		One final note.  This class is declared final to
 *		prevent users from trying to extend it, and creating
 *		a race condition when wait is called in the move
 *		method.  It is dangerous to extend this class, and
 *		this prevents mindless extension for reuse with 
 *		unintended consequences.
 */

public final class MoveController {
    private Path myPath;
    private boolean doNotify = false;

    /**
     *  The move method simply saves the parameter path into
     *  the variable myPath, and does the wait, much like its
     *  procedural cousin.  
     */
    public synchronized void move(Path path) {
        myPath = path;
        try {
            doNotify = true;
            wait();
        } catch (InterruptedException e) {
        }
    }

    /**
     *  Check if the path has more steps in it.  Note that it is
     *  not necessary to call this method in the using class as the
     *  notification is handled in the nextPosition call, but since
     *  the myPath variable is encapsulated, this delegate call allows
     *  the program to still have access to this method if it needs it.
     */
    public boolean hasMoreSteps() {
        return myPath.hasMoreSteps();
    }
   
    /**
     *  This method gets the nextPostion from the myPath variable, and
     *  does a notify if the end of the path has been reached.  Note 
     *  that this method does more than simply get the nextPosition, 
     *  but the name was kept from the Path to simplify adding 
     *  MoveController objects into existing programs.
     */
    public synchronized Point nextPosition() {
        if (myPath != null && myPath.hasMoreSteps()) {
            return myPath.nextPosition();
        }
        else if (myPath != null) {
            if (doNotify) {
                doNotify = false;
                notify();
            }
            return myPath.nextPosition();
        }
        else
            return new Point(0,0);
    }

}
