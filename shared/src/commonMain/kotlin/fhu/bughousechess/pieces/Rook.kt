package fhu.bughousechess.pieces

import fhu.bughousechess.Move

class Rook(color: String?) : Piece() {
    init {
        this.color = color
        type = "rook"
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
        var inbetween = false
        for (i in y + 1..7) {
            for (j in y + 1 until i) {
                if (!positions!![x]!![j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x]!![i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(x, y, x, i, "move"))
                }
            }
            if (positions[x]!![i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(x, y, x, i, "take"))
                }
            }
        }
        inbetween = false
        for (i in y - 1 downTo -1 + 1) {
            for (j in y - 1 downTo i + 1) {
                if (!positions!![x]!![j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x]!![i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(x, y, x, i, "move"))
                }
            }
            if (positions[x]!![i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(x, y, x, i, "take"))
                }
            }
        }
        inbetween = false
        for (i in x + 1..7) {
            for (j in x + 1 until i) {
                if (!positions!![j]!![y]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![i]!![y]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(x, y, i, y, "move"))
                }
            }
            if (positions[i]!![y]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(x, y, i, y, "take"))
                }
            }
        }
        inbetween = false
        for (i in x - 1 downTo -1 + 1) {
            for (j in x - 1 downTo i + 1) {
                if (!positions!![j]!![y]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![i]!![y]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(x, y, i, y, "move"))
                }
            }
            if (positions[i]!![y]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(x, y, i, y, "take"))
                }
            }
        }
        return moves
    }
}