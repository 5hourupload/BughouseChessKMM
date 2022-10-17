package fhu.bughousechess.pieces

import fhu.bughousechess.GameStateManager
import fhu.bughousechess.Move

class King(color: String?) : Piece() {
    init {
        this.color = color
        type = "king"
        wasPawn = false
        empty = false
    }

    override fun getMoves(
        positions: Array<Array<Piece>>,
        x: Int,
        y: Int,
        boardNumber: Int
    ): Set<Move?>? {
        val moves: MutableSet<Move?> = HashSet()
        if (x + 1 < 8) {
            if (positions!![x + 1]!![y]!!.empty) {
                moves.add(Move(positions, x, y, x + 1, y, "move"))
                if (positions[6]!![0]!!.empty && positions[4]!![0]!!.type == "king" && positions[4]!![0]!!.color == "white") {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    if (boardNumber == 0) {
                        if (GameStateManager.whiteCastleKing1 && positions[6]!![0]!!.empty && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                4,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                5,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                6,
                                0,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 6, 0, "whiteKingCastle"))
                        }
                    } else {
                        if (GameStateManager.whiteCastleKing2 && positions[6]!![0]!!.empty && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                4,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                5,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                6,
                                0,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 6, 0, "whiteKingCastle"))
                        }
                    }
                }
                if (positions[6]!![7]!!.empty && positions[4]!![7]!!.type == "king" && positions[4]!![7]!!.color == "black") {
                    //setting those empty positions to "white" so that they can be checked whether
                    //they are in check or not
                    if (boardNumber == 0) {
                        if (GameStateManager.whiteCastleKing1 && positions[6]!![7]!!.empty && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                4,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                5,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                6,
                                7,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 6, 7, "blackKingCastle"))
                        }
                    } else {
                        if (GameStateManager.whiteCastleKing2 && positions[6]!![7]!!.empty && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                4,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                5,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                6,
                                7,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 6, 7, "blackKingCastle"))
                        }
                    }
                }
            }
            if (positions[x + 1]!![y]!!.isOpposite(this)) {
                moves.add(Move(positions, x, y, x + 1, y, "take"))
            }
            if (y + 1 < 8) {
                if (positions[x + 1]!![y + 1]!!.empty) {
                    moves.add(Move(positions, x, y, x + 1, y + 1, "move"))
                }
                if (positions[x + 1]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x + 1, y + 1, "take"))
                }
            }
            if (y - 1 > -1) {
                if (positions[x + 1]!![y - 1]!!.empty) {
                    moves.add(Move(positions, x, y, x + 1, y - 1, "move"))
                }
                if (positions[x + 1]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x + 1, y - 1, "take"))
                }
            }
        }
        if (x - 1 > -1) {
            if (positions!![x - 1]!![y]!!.empty) {
                moves.add(Move(positions, x, y, x - 1, y, "move"))
                if (positions[2]!![0]!!.empty && positions[1]!![0]!!.empty && positions[4]!![0]!!.type == "king") {
                    if (boardNumber == 0) {
                        if (GameStateManager.whiteCastleQueen1 && positions[2]!![0]!!.empty && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                1,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                2,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                3,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                4,
                                0,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 2, 0, "whiteQueenCastle"))
                        }
                    } else {
                        if (GameStateManager.whiteCastleQueen2 && positions[2]!![0]!!.empty && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                1,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                2,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                3,
                                0,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "white",
                                positions,
                                4,
                                0,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 2, 0, "whiteQueenCastle"))
                        }
                    }
                }
                if (positions[2]!![7]!!.empty && positions[1]!![7]!!.empty && positions[4]!![7]!!.type == "king") {
                    if (boardNumber == 0) {
                        if (GameStateManager.whiteCastleQueen1 && positions[2]!![7]!!.empty && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                1,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                2,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                3,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                4,
                                7,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 2, 7, "blackQueenCastle"))
                        }
                    } else {
                        if (GameStateManager.whiteCastleQueen2 && positions[2]!![7]!!.empty && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                1,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                2,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                3,
                                7,
                                boardNumber
                            ) && !GameStateManager.castleCheckCheck(
                                "black",
                                positions,
                                4,
                                7,
                                boardNumber
                            )
                        ) {
                            moves.add(Move(positions, x, y, 2, 7, "blackQueenCastle"))
                        }
                    }
                }
            }
            if (positions[x - 1]!![y]!!.isOpposite(this)) {
                moves.add(Move(positions, x, y, x - 1, y, "take"))
            }
            if (y + 1 < 8) {
                if (positions[x - 1]!![y + 1]!!.empty) {
                    moves.add(Move(positions, x, y, x - 1, y + 1, "move"))
                }
                if (positions[x - 1]!![y + 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x - 1, y + 1, "take"))
                }
            }
            if (y - 1 > -1) {
                if (positions[x - 1]!![y - 1]!!.empty) {
                    moves.add(Move(positions, x, y, x - 1, y - 1, "move"))
                }
                if (positions[x - 1]!![y - 1]!!.isOpposite(this)) {
                    moves.add(Move(positions, x, y, x - 1, y - 1, "take"))
                }
            }
        }
        if (y + 1 < 8) {
            if (positions!![x]!![y + 1]!!.empty) {
                moves.add(Move(positions, x, y, x, y + 1, "move"))
            }
            if (positions[x]!![y + 1]!!.isOpposite(this)) {
                moves.add(Move(positions, x, y, x, y + 1, "take"))
            }
        }
        if (y - 1 > -1) {
            if (positions!![x]!![y - 1]!!.empty) {
                moves.add(Move(positions, x, y, x, y - 1, "move"))
            }
            if (positions[x]!![y - 1]!!.isOpposite(this)) {
                moves.add(Move(positions, x, y, x, y - 1, "take"))
            }
        }
        return moves
    }
}