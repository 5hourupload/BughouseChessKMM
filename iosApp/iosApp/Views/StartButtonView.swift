//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct StartButtonView: View {
    
    @ObservedObject var gameManager: GameManager
    
    init(gameManager: GameManager) {
        self.gameManager = gameManager
    }
    
    var body: some View
    {
        let squareSize = getSquareSizeGlobal()

        Button(gameManager.controlButtonText) {
            gameManager.controlButtonClick()
        }.frame(width: squareSize * 2, height: squareSize * 0.75).foregroundColor(.black)
            .background(Color.gray)
            .cornerRadius(10)

    }
}


