package fhu.bughousechess.pieces

import fhu.bughousechess.Move

class Queen(color: String?) : Piece() {
    init {
        this.color = color
        type = "queen"
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
                    moves.add(Move(positions, x, y, x, i, "move"))
                }
            }
            if (positions[x]!![i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x, i, "take"))
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
                    moves.add(Move(positions, x, y, x, i, "move"))
                }
            }
            if (positions[x]!![i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x, i, "take"))
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
                    moves.add(Move(positions, x, y, i, y, "move"))
                }
            }
            if (positions[i]!![y]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, i, y, "take"))
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
                    moves.add(Move(positions, x, y, i, y, "move"))
                }
            }
            if (positions[i]!![y]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, i, y, "take"))
                }
            }
        }
        inbetween = false
        var z = x
        if (y > x) {
            z = y
        }
        for (i in 1 until 8 - z) {
            for (j in 1 until i) {
                if (!positions!![x + j]!![y + j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x + i]!![y + i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x + i, y + i, "move"))
                }
            }
            if (positions[x + i]!![y + i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x + i, y + i, "take"))
                }
            }
        }
        inbetween = false
        z = if (x < y) {
            x
        } else {
            y
        }
        for (i in 1 until z + 1) {
            for (j in 1 until i) {
                if (!positions!![x - j]!![y - j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x - i]!![y - i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x - i, y - i, "move"))
                }
            }
            if (positions[x - i]!![y - i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x - i, y - i, "take"))
                }
            }
        }
        inbetween = false
        z = if (7 - x < y) {
            7 - x
        } else {
            y
        }
        for (i in 1 until z + 1) {
            for (j in 1 until i) {
                if (!positions!![x + j]!![y - j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x + i]!![y - i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x + i, y - i, "move"))
                }
            }
            if (positions[x + i]!![y - i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x + i, y - i, "take"))
                }
            }
        }
        inbetween = false
        z = if (7 - y < x) {
            7 - y
        } else {
            x
        }
        for (i in 1 until z + 1) {
            for (j in 1 until i) {
                if (!positions!![x - j]!![y + j]!!.empty) {
                    inbetween = true
                }
            }
            if (positions!![x - i]!![y + i]!!.empty) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x - i, y + i, "move"))
                }
            }
            if (positions[x - i]!![y + i]!!.isOpposite(this)) {
                if (!inbetween) {
                    moves.add(Move(positions, x, y, x - i, y + i, "take"))
                }
            }
        }
        return moves
    }
}