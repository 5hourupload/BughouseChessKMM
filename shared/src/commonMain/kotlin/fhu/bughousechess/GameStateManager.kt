package fhu.bughousechess


import fhu.bughousechess.pieces.*

class GameStateManager {
    private val positions = Array(2) { Array<Array<Piece>>(8) { arrayOf<Piece>(Empty(),Empty(),Empty(),Empty(),Empty(),Empty(),Empty(),Empty()) } }
    @JvmField
    var roster1p = arrayOf<Piece>(
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty()
    )
    @JvmField
    var roster2p = arrayOf<Piece>(
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty()
    )
    @JvmField
    var roster3p = arrayOf<Piece>(
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty()
    )
    @JvmField
    var roster4p = arrayOf<Piece>(
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty(),
        Empty()
    )
    @JvmField
    var turn = intArrayOf(1, 1)
    @JvmField
    var gameState = GameState.PREGAME
    var turnSave1 = 0
    var turnSave2 = 0
    @JvmField
    var gameOver = false
    @JvmField
    var gameOverSide = -1
    @JvmField
    var gameOverType = -1

    enum class GameState {
        PREGAME, PLAYING, PAUSED
    }

    init {
        resetEnPassantTracker()
        setStartingPieces(positions)
    }

    fun resetEnPassantTracker() {
        for (i in 0..7) {
            for (j in 0..3) {
                enP[i][j] = "0000"
            }
        }
    }

    private fun setStartingPieces(positions: Array<Array<Array<Piece>>>) {
        for (b in 0..1) {
            for (i in 0..7) {
                for (j in 0..7) {
                    positions[b][i]!![j] = Empty()
                }
            }
            for (i in 0..7) {
                positions[b][i]!![1] = Pawn("white")
            }
            for (i in 0..7) {
                positions[b][i]!![6] = Pawn("black")
            }
            positions[b][0]!![0] = Rook("white")
            positions[b][7]!![0] = Rook("white")
            positions[b][0]!![7] = Rook("black")
            positions[b][7]!![7] = Rook("black")
            positions[b][1]!![0] = Knight("white")
            positions[b][6]!![0] = Knight("white")
            positions[b][1]!![7] = Knight("black")
            positions[b][6]!![7] = Knight("black")
            positions[b][2]!![0] = Bishop("white")
            positions[b][5]!![0] = Bishop("white")
            positions[b][2]!![7] = Bishop("black")
            positions[b][5]!![7] = Bishop("black")
            positions[b][3]!![0] = Queen("white")
            positions[b][3]!![7] = Queen("black")
            positions[b][4]!![0] = King("white")
            positions[b][4]!![7] = King("black")
        }
        for (i in 0..29) {
            roster1p[i] = Empty()
            roster2p[i] = Empty()
            roster3p[i] = Empty()
            roster4p[i] = Empty()
        }
    }

    fun performMove(moveType: String, x: Int, y: Int, x1: Int, y1: Int, boardNumber: Int) {
        val color = positions[boardNumber][x]!![y]!!.color
        //?? not sur what this is for, possible a fringe case catcher?
        if (positions[boardNumber][x]!![y]!!.empty) {
            turn[0] = 3
            turn[1] = 3
            gameState = GameState.PAUSED
            return
        }
        turnChange(positions[boardNumber][x]!![y]!!.color, boardNumber)
        if (positions[boardNumber][x]!![y]!!.color == "white" && positions[boardNumber][x]!![y]!!.type == "pawn" && y == 1 && y1 == 3) {
            if (boardNumber == 0) enP[x][0] = "1" + Integer.toString(board1Turn)
            if (boardNumber == 1) enP[x][2] = "1" + Integer.toString(board2Turn)
        }
        if (positions[boardNumber][x]!![y]!!.color == "black" && positions[boardNumber][x]!![y]!!.type == "pawn" && y == 6 && y1 == 4) {
            if (boardNumber == 0) enP[x][1] = "1" + Integer.toString(board1Turn)
            if (boardNumber == 1) enP[x][3] = "1" + Integer.toString(board2Turn)
        }
        if (moveType == "take" || moveType == "whiteEnP" || moveType == "blackEnP") {
            addToRoster(x1, y1, boardNumber)
        }
        switchPositions(moveType, positions[boardNumber], x, y, x1, y1)
        endOfMoveChecks(color, boardNumber)
    }

    fun performRosterMove(i: Int, x: Int, y: Int, boardNumber: Int) {
        var roster: Array<Piece> = roster1p
        if (boardNumber == 0 && turn[boardNumber] == 1) roster = roster1p
        if (boardNumber == 0 && turn[boardNumber] == 2) roster = roster2p
        if (boardNumber == 1 && turn[boardNumber] == 1) roster = roster4p
        if (boardNumber == 1 && turn[boardNumber] == 2) roster = roster3p
        if (roster!![i]!!.empty) {
            turn[0] = 3
            turn[1] = 3
            gameState = GameState.PAUSED
            return
        }
        turnChange(roster[i]!!.color, boardNumber)
        positions[boardNumber][x]!![y] = roster[i]
        roster[i] = Empty()
        endOfMoveChecks(positions[boardNumber][x]!![y]!!.color, boardNumber)
    }

    private fun turnChange(prevColor: String?, boardNumber: Int) {
        if (boardNumber == 0) {
            if (prevColor == "white") {
                if (gameState == GameState.PAUSED) {
                    turnSave1 = 2
                } else {
                    turn[0] = 2
                }
            } else {
                if (gameState == GameState.PAUSED) {
                    turnSave1 = 1
                } else {
                    turn[0] = 1
                }
            }
            board1Turn++
        } else {
            if (prevColor == "white") {
                if (gameState == GameState.PAUSED) {
                    turnSave2 = 2
                } else {
                    turn[1] = 2
                }
            } else {
                if (gameState == GameState.PAUSED) {
                    turnSave2 = 1
                } else {
                    turn[1] = 1
                }
            }
            board2Turn++
        }
    }

    fun checkIfMoveResultsInCheck(
        moveType: String,
        x: Int,
        y: Int,
        x1: Int,
        y1: Int,
        boardNumber: Int
    ): Boolean {
        if (checking) {
            val temp1 = positions[boardNumber][0]!!.clone()
            val temp2 = positions[boardNumber][1]!!.clone()
            val temp3 = positions[boardNumber][2]!!.clone()
            val temp4 = positions[boardNumber][3]!!.clone()
            val temp5 = positions[boardNumber][4]!!.clone()
            val temp6 = positions[boardNumber][5]!!.clone()
            val temp7 = positions[boardNumber][6]!!.clone()
            val temp8 = positions[boardNumber][7]!!.clone()
            val tempPositions =
                arrayOf<Array<Piece>>(temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8)
            switchPositions(moveType, tempPositions, x, y, x1, y1)
            if (turn[boardNumber] == 1) if (inCheck(
                    tempPositions,
                    "white",
                    boardNumber
                )
            ) return true
            if (turn[boardNumber] == 2) if (inCheck(
                    tempPositions,
                    "black",
                    boardNumber
                )
            ) return true
        }
        return false
    }

    fun rosterMoveIsLegal(rosterPiece: Piece, x: Int, y: Int, boardNumber: Int): Boolean {
        if (!placing) {
            val old = positions[boardNumber][x]!![y]
            positions[boardNumber][x]!![y] = rosterPiece
            if (rosterPiece!!.color == "white") {
                if (inCheck(positions[boardNumber], "black", boardNumber)) {
                    positions[boardNumber][x]!![y] = old
                    return false
                }
            } else {
                if (inCheck(positions[boardNumber], "white", boardNumber)) {
                    positions[boardNumber][x]!![y] = old
                    return false
                }
            }
            positions[boardNumber][x]!![y] = old
        }
        if (checking) {
            val old = positions[boardNumber][x]!![y]
            positions[boardNumber][x]!![y] = rosterPiece
            if (rosterPiece!!.color == "white") {
                if (inCheck(positions[boardNumber], "white", boardNumber)) {
                    positions[boardNumber][x]!![y] = old
                    return false
                }
            } else {
                if (inCheck(positions[boardNumber], "black", boardNumber)) {
                    positions[boardNumber][x]!![y] = old
                    return false
                }
            }
            positions[boardNumber][x]!![y] = old
        }
        return true
    }

    fun checkIfKingsStillStanding(boardNumber: Int) {
        var player1 = false
        var player2 = false
        var player3 = false
        var player4 = false
        for (i in 0..7) {
            for (j in 0..7) {
                if (boardNumber == 0) {
                    if (positions[boardNumber][i]!![j]!!.color == "white" && positions[boardNumber][i]!![j]!!.type == "king") {
                        player1 = true
                    }
                    if (positions[boardNumber][i]!![j]!!.color == "black" && positions[boardNumber][i]!![j]!!.type == "king") {
                        player2 = true
                    }
                }
                if (boardNumber == 1) {
                    if (positions[boardNumber][i]!![j]!!.color == "white" && positions[boardNumber][i]!![j]!!.type == "king") {
                        player3 = true
                    }
                    if (positions[boardNumber][i]!![j]!!.color == "black" && positions[boardNumber][i]!![j]!!.type == "king") {
                        player4 = true
                    }
                }
            }
        }
        if (boardNumber == 0) {
            if (!player2) {
                gameEndProcedures(0, 1)
            }
            if (!player1) {
                gameEndProcedures(1, 1)
            }
        }
        if (boardNumber == 1) {
            if (!player4) {
                gameEndProcedures(1, 1)
            }
            if (!player3) {
                gameEndProcedures(0, 1)
            }
        }
    }

    fun updateLegalCastlingVariables(boardNumber: Int) {
        if (boardNumber == 0) {
            if (positions[boardNumber][0]!![0]!!.color != "white" || positions[boardNumber][0]!![0]!!.type != "rook") {
                whiteCastleQueen1 = false
            }
            if (positions[boardNumber][7]!![0]!!.color != "white" || positions[boardNumber][7]!![0]!!.type != "rook") {
                whiteCastleKing1 = false
            }
            if (positions[boardNumber][4]!![0]!!.color != "white" || positions[boardNumber][4]!![0]!!.type != "king") {
                whiteCastleKing1 = false
                whiteCastleQueen1 = false
            }
            if (positions[boardNumber][0]!![7]!!.color != "black" || positions[boardNumber][0]!![7]!!.type != "rook") {
                blackCastleQueen1 = false
            }
            if (positions[boardNumber][7]!![7]!!.color != "black" || positions[boardNumber][7]!![7]!!.type != "rook") {
                blackCastleKing1 = false
            }
            if (positions[boardNumber][4]!![7]!!.color != "black" || positions[boardNumber][4]!![7]!!.type != "king") {
                blackCastleKing1 = false
                blackCastleQueen1 = false
            }
        } else {
            if (positions[boardNumber][0]!![0]!!.color != "white" || positions[boardNumber][0]!![0]!!.type != "rook") {
                whiteCastleQueen2 = false
            }
            if (positions[boardNumber][7]!![0]!!.color != "white" || positions[boardNumber][7]!![0]!!.type != "rook") {
                whiteCastleKing2 = false
            }
            if (positions[boardNumber][4]!![0]!!.color != "white" || positions[boardNumber][4]!![0]!!.type != "king") {
                whiteCastleKing2 = false
                whiteCastleQueen2 = false
            }
            if (positions[boardNumber][0]!![7]!!.color != "black" || positions[boardNumber][0]!![7]!!.type != "rook") {
                blackCastleQueen2 = false
            }
            if (positions[boardNumber][7]!![7]!!.color != "black" || positions[boardNumber][7]!![7]!!.type != "rook") {
                blackCastleKing2 = false
            }
            if (positions[boardNumber][4]!![7]!!.color != "black" || positions[boardNumber][4]!![7]!!.type != "king") {
                blackCastleKing2 = false
                blackCastleQueen2 = false
            }
        }
    }

    fun checkForCheckmate(color: String, boardNumber: Int) {
        var checkmate = true
        for (x in 0..7) {
            for (y in 0..7) {
                if (positions[boardNumber][x]!![y]!!.color == color) {
                    val moves = positions[boardNumber][x]!![y]!!.getMoves(
                        positions[boardNumber],
                        x,
                        y,
                        boardNumber
                    )
                    for (m in moves!!) {
                        if (!checkIfMoveResultsInCheck(m!!.type, x, y, m.x1, m.y1, boardNumber)) {
                            checkmate = false
                            break
                        }
                    }
                }
            }
        }
        var piecesOnRoster = false
        var roster: Array<Piece> = roster1p
        if (boardNumber == 0 && color == "white") roster = roster1p
        if (boardNumber == 0 && color == "black") roster = roster2p
        if (boardNumber == 1 && color == "white") roster = roster4p
        if (boardNumber == 1 && color == "black") roster = roster3p
        for (i in 0..29) {
            if (!roster!![i]!!.empty) {
                piecesOnRoster = true
                val moves = roster[i]!!
                    .getRosterMoves(positions[boardNumber], roster, i)
                for (m in moves) {
                    if (rosterMoveIsLegal(roster[i], m.x1, m.y1, boardNumber)) {
                        checkmate = false
                        break
                    }
                }
            }
        }
        if (!piecesOnRoster) {
            roster!![0] = Queen(color)
            val moves = roster[0].getRosterMoves(positions[boardNumber], roster, 0)
            for (m in moves) {
                if (rosterMoveIsLegal(roster[0], m.x1, m.y1, boardNumber)) {
                    checkmate = false
                    break
                }
            }
            roster[0] = Empty()
        }
        if (checkmate) {
            if (boardNumber == 0 && color == "white") gameEndProcedures(1, 0)
            if (boardNumber == 0 && color == "black") gameEndProcedures(0, 0)
            if (boardNumber == 1 && color == "white") gameEndProcedures(0, 0)
            if (boardNumber == 1 && color == "black") gameEndProcedures(1, 0)
        }
    }

    fun gameEndProcedures(side: Int, type: Int) {
        gameState = GameState.PREGAME
        gameOver = true
        gameOverSide = side
        gameOverType = type
    }

    fun getCurrentRosterArray(boardNumber: Int): Array<Piece> {
        if (boardNumber == 0) {
            if (turn[boardNumber] == 1) return roster1p
            if (turn[boardNumber] == 2) return roster2p
        }
        if (boardNumber == 1) {
            if (turn[boardNumber] == 1) return roster4p
            if (turn[boardNumber] == 2) return roster3p
        }
        return roster4p //should never get here
    }

    fun promote(x: Int, y: Int, piece: Piece, boardNumber: Int) {
        positions[boardNumber][x]!![y] = piece
        positions[boardNumber][x]!![y]!!.wasPawn = true
    }

    fun pause(boardNumber: Int) {
        if (boardNumber == 0) {
            turnSave1 = turn[0]
            turn[0] = 3
        }
        if (boardNumber == 1) {
            turnSave2 = turn[1]
            turn[1] = 3
        }
    }

    fun pause() {
        turnSave1 = turn[0]
        turn[0] = 3
        turnSave2 = turn[1]
        turn[1] = 3
        gameState = GameState.PAUSED
    }

    fun resume(boardNumber: Int) {
        if (boardNumber == 0) {
            turn[0] = turnSave1
        }
        if (boardNumber == 1) {
            turn[1] = turnSave2
        }
    }

    fun resume() {
        gameState = GameState.PLAYING
        turn[0] = turnSave1
        turn[1] = turnSave2
    }

    fun start() {
        gameState = GameState.PLAYING
        turn[0] = 1
        turn[1] = 1
    }

    fun getPositions(boardNumber: Int): Array<Array<Piece>> {
        return positions[boardNumber]
    }

    private fun endOfMoveChecks(color: String?, boardNumber: Int) {
        updateLegalCastlingVariables(boardNumber)
        checkIfKingsStillStanding(boardNumber)
        checkForCheckmate(if (color == "white") "black" else "white", boardNumber)
    }

    fun addToRoster(x1: Int, y1: Int, boardNumber: Int) {
        if (boardNumber == 0) {
            if (positions[boardNumber][x1]!![y1]!!.color == "white") {
                addToRosterShit(roster4p, x1, y1, boardNumber)
            }
            if (positions[boardNumber][x1]!![y1]!!.color == "black") {
                addToRosterShit(roster3p, x1, y1, boardNumber)
            }
        } else {
            if (positions[boardNumber][x1]!![y1]!!.color == "white") {
                addToRosterShit(roster1p, x1, y1, boardNumber)
            }
            if (positions[boardNumber][x1]!![y1]!!.color == "black") {
                addToRosterShit(roster2p, x1, y1, boardNumber)
            }
        }
    }

    private fun addToRosterShit(rosterp: Array<Piece>, x1: Int, y1: Int, boardNumber: Int) {
        for (i in 0..29) {
            if (rosterp[i]!!.empty) {
                if (reverting) {
                    if (positions[boardNumber][x1]!![y1]!!.wasPawn) {
                        if (positions[boardNumber][x1]!![y1]!!.color == "white") {
                            rosterp[i] = Pawn("white")
                            break
                        }
                        if (positions[boardNumber][x1]!![y1]!!.color == "black") {
                            rosterp[i] = Pawn("black")
                            break
                        }
                    }
                }
                rosterp[i] = positions[boardNumber][x1]!![y1]
                break
            }
        }
    }

    companion object {
        var whiteCastleQueen1 = true
        var whiteCastleKing1 = true
        var blackCastleQueen1 = true
        var blackCastleKing1 = true
        var whiteCastleQueen2 = true
        var whiteCastleKing2 = true
        var blackCastleQueen2 = true
        var blackCastleKing2 = true
        @JvmField
        var checking = true
        @JvmField
        var placing = true
        @JvmField
        var reverting = true
        @JvmField
        var firstrank = false
        var enP = Array(8) { arrayOf<String>("","","","") }
        var board1Turn = 0
        var board2Turn = 0
        @JvmField
        var position1 = true
        @JvmField
        var position2 = true
        @JvmField
        var position3 = true
        @JvmField
        var position4 = true
        @JvmStatic
        fun switchPositions(
            moveType: String,
            positions: Array<Array<Piece>>,
            x: Int,
            y: Int,
            x1: Int,
            y1: Int
        ) {
            if (moveType == "whiteKingCastle") {
                positions[x1]!![y1] = positions[x]!![y]
                positions[x]!![y] = Empty()
                positions[7]!![0] = Empty()
                positions[5]!![0] = Rook("white")
                return
            }
            if (moveType == "whiteQueenCastle") {
                positions[x1]!![y1] = positions[x]!![y]
                positions[x]!![y] = Empty()
                positions[0]!![0] = Empty()
                positions[3]!![0] = Rook("white")
                return
            }
            if (moveType == "blackKingCastle") {
                positions[x1]!![y1] = positions[x]!![y]
                positions[x]!![y] = Empty()
                positions[7]!![7] = Empty()
                positions[5]!![7] = Rook("black")
                return
            }
            if (moveType == "blackQueenCastle") {
                positions[x1]!![y1] = positions[x]!![y]
                positions[x]!![y] = Empty()
                positions[0]!![7] = Empty()
                positions[3]!![7] = Rook("black")
                return
            }
            if (moveType == "whiteEnP") {
                positions[x1]!![4] = Empty()
                positions[x1]!![5] = positions[x]!![y]
                positions[x]!![y] = Empty()
                return
            }
            if (moveType == "blackEnP") {
                positions[x1]!![3] = Empty()
                positions[x1]!![2] = positions[x]!![y]
                positions[x]!![y] = Empty()
                return
            }
            positions[x1]!![y1] = positions[x]!![y]
            positions[x]!![y] = Empty()
        }

        /**
         * Along with blackInCheck, this requires the positions and boardnumber variable because
         * the positions might be temporary, separate from the class variable
         */
        @JvmStatic
        fun inCheck(positions: Array<Array<Piece>>, color: String, boardNumber: Int): Boolean {
            for (i in 0..7) {
                for (j in 0..7) {
                    if (positions[i]!![j]!!.color == color && positions[i]!![j]!!.type == "king" &&
                        checkCheck(positions, i, j, boardNumber)
                    ) {
                        return true
                    }
                }
            }
            return false
        }

        fun checkCheck(
            positions: Array<Array<Piece>>,
            x: Int,
            y: Int,
            boardNumber: Int
        ): Boolean {
            for (i in 0..7) {
                for (j in 0..7) {
                    if (positions[i]!![j]!!.isOpposite(positions[x]!![y]!!)) {
                        val moves = positions[i]!![j]!!.getMoves(positions, i, j, boardNumber)
                        for (m in moves!!) {
                            if (m!!.type == "take" && m.x1 == x && m.y1 == y) return true
                        }
                    }
                }
            }
            return false
        }

        /**
         * This method is just for checking if kings can castle
         * If we use the regular check checking method, then there will be a stack overflow
         * king's get moves call check check, which ends up calling king's get moves, etc
         * I think it is extremely unlikely that an opposing king would prevent castling
         */
        fun castleCheckCheck(
            color: String,
            positions: Array<Array<Piece>>,
            x: Int,
            y: Int,
            boardNumber: Int
        ): Boolean {
            var oppositeColor = "black"
            if (color == "black") oppositeColor = "white"
            for (i in 0..7) {
                for (j in 0..7) {
                    if (positions[i]!![j]!!.color == oppositeColor) {
                        if (positions[i]!![j]!!.type != "king") {
                            val moves = positions[i]!![j]!!.getMoves(positions, i, j, boardNumber)
                            for (m in moves!!) {
                                if (m!!.type == "take" && m.x1 == x && m.y1 == y) return true
                            }
                        }
                    }
                }
            }
            return false
        }
    }
}