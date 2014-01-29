Othello is based on following protocol
==========================================================================================================================

Ref: IAGO paper

Othello
File: Othello Specifications
Author: K.R. Sloan
Last Modified: 20 May 1991
Purpose: describe I/O behavior of Othello programs

                        Othello Specifications

0) The basic rules for Othello are as laid out in:

    Rosenbloom, Paul S. "A World-Championship-Level Othello Program",
    Artificial Intelligence 19 (1982) 279-320.

    a) the board is 8x8;
       columns labelled a-h (from left to right);
       rows labelled 1-8 (from top to bottom).
       NOTE: column names are in lower case; UPPERCASE IS OK, but boring.
    b) Initial position:
       White pieces on d4 and e5,
       Black pieces on d5 and e4.
    c) Black moves first.
    d) Game is over when NEITHER player can move.
    e) "pass" is a legal move iff you have no other legal move.
    f) time will be kept by the Referee; each player is limited to
       30 minutes of wall-clock time.

1) All communication with your program is via "stdin" and "stdout".  Note
that "stderr" may be going to the same place as "stdout", so anything
written to "stderr" must obey the syntax rules which apply to "stdout".

2) INPUT to your program will consist of:

    a) (I W) or (I B): informs you that you are playing WHITE or BLACK.
    b) (W c r) or (B c r): informs you that your opponent has played at
        [c r].  If you are W, then you should only accept (B c r).  If you
        are B, then you should only accept (W c r).  In either case, you
        must verify that the move is legal.  
    c) (W) or (B): informs you that your opponent has passed, because he has
        no legal move.  You must verify that this is the case.
    d) (n): informs you that your opponent claims that the game is over,
        and has the value n for BLACK.  You must verify that this claim is
        correct.
    e) (C s): informs you that your opponent has commented "s".  This is
        a general escape mechanism.  You should ignore these inputs.
        The Referee may filter them out for you, but you should not
        count on this.
    f) (play): used to start up lisp implementations.  Your program
        must IGNORE THIS.

3) OUTPUT from your program MUST BE LIMITED TO:

    a) (R W) or (R B): in response to an (I color) input, indicating
        that you are ready for the first move.
    b) (W c r) or (B c r): making a move. 
    c) (W) or (B): passing because you have no legal move.
       NOTE: if you are WHITE, you should NOT issue a (W) at the
       start of the game; the Referee will do that.
    d) (n): claiming the end of the game.
    e) (C s): commenting.  Use this format for debugging output, comments
        on the game, evaluations of the position, printing the board, etc.  

4) ALL inputs and outputs may (should) be terminated by <nl>.  These are to
be ignored, but may be necessary to push the data through the pipe.
Everything written by your program should be a valid S-expression.  Beware
of special characters (especially ".", ",", and ":"), and DO NOT use
whitespace (multiple spaces, tabs, etc.) for formatting.

5) When commenting on a move (for example, when printing out the board),
make all of your comments first, AND THEN make your move.  This avoids a
subtle synchronization problem with the Referee.  In other words, your
move should be the LAST thing that you output on every turn.

6) Here is an example session; lines beginning with ">>>" are inputs, other
lines are outputs; the right-hand column is commentary:.

                                     Comment   
>>>(I B)                         I'll play White - you play Black
(R B)                            Ready to play Black 
>>>(W)                           I can't move - it's your turn
(C (First move is forced eh?))
(B d 3)                                  1. B-d3
>>>(C (wow! what a surprise!))
>>>(C (the diagonal opening)) (W c 3)    1. ...   W-c3
(C (oops...)) (B c 4)                    2. B-c4
>>>(C (I see what you mean!)) (W c 5)    2. ...   W-c5
(B a 1)                                  3. B-a1??? 
>>>(C (impossible!))(-64)                That was an illegal move, I win
(C (Director!)) (64)                     What!?! somebody's confused...


In general, the end of a game OUGHT to look like:

>>>(W a 1)                        Finally - I got that corner!
(B)                               No moves
>>>(W a 8)                        gobble, gobble
(B)                               Still no moves
>>>(-43)                          I can't move either - game over and I win
(-43)                             Right you are - how about a game of
                                    Global Thermonuclear War?

6) If you receive an illegal move, or something which you don't
recognize, it is acceptable to reply (64) or (-64) as above.  However,
it is better to issue a (C comment) complaining about the problem, and
re-try the read - this will save you much grief when running your
program interactively.

7) In general, your program will be run as a separate process, from my
account.  You are encouraged to hold "training matches" to work out the
bugs.  The best testing method is to play against your program yourself,
or run two programs separately and manually referee the game.

8) The initial tournament will be a Swiss tournament.  To submit an
entry, send me mail and point me at a directory containing the source
code, a makefile, and instructions for running your program.  I may
experiment with the programs in order to eliminate gross I/O errors (and
give you a chance to correct them), and to establish seedings.  Early
entries receive the benefit of some beta-testing.  Null where prohibited
by law, decisions of the judges are final.

9) Other tournaments may be run, as time permits.  In particular, as
soon as the Swiss is complete, I will run a single round robin.  If it
still looks interesting, I'll fill that out to a complete double-round
robin.
