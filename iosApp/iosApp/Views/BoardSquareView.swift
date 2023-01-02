//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct BoardSquareView: View {
    var x: Int
    var y: Int
    var boardNumber: Int
    var gameManager: GameManager
    var backgroundImage: UIImage
    @State var dragging: Bool = false
    @State private var location: CGPoint = CGPoint(x: 0, y: 0)

    
    init(x: Int, y: Int, boardNumber: Int, gameManager: GameManager) {
        self.x = x
        self.y = y
        self.boardNumber = boardNumber
        self.gameManager = gameManager
        
        var squareSize: CGFloat = getSquareSizeGlobal()

        if ((x + y).isMultiple(of: 2)) {
            backgroundImage = (UIImage(named: "black")?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!
        }
        else {
            backgroundImage = (UIImage(named: "white")?.resizeImageTo(size: CGSize(width: squareSize, height: squareSize)))!
        }
    }
    
    var body: some View
    {
        
        ZStack{
            Image(uiImage: backgroundImage)
            Image(uiImage: gameManager.board[boardNumber][x][y].getCosmetic())
        }.onTapGesture {
            gameManager.processMove(x: x,y: y,boardNumber: boardNumber)
        }

    }
    
}



