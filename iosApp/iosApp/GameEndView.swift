//
//  Square.swift
//  iosApp
//
//  Created by Alan Shen on 10/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct GameEndView: View {
    
 
    @State var show = false
    var gameManager: GameManager
    
    init(gameManager: GameManager, show: Bool) {
        self.show = show
        
        self.gameManager = gameManager
    }
    
    var body: some View
    {
        
//        if true {
            if gameManager.showGameEndScreen {
            VStack(spacing: 0)
              {
                  Text("GAME OVER")
                      .font(.system(size: 30, weight: .bold))
                  Text("Checkmate")
                      .font(.system(size: 20, weight: .bold))
                  HStack()
                  {
                      Text("Winner")
                          .font(.system(size: 60, weight: .bold))
                          .rotationEffect(.degrees(90.0))
                      Spacer()
                      Text("Loser")
                          .font(.system(size: 60, weight: .bold))
                          .rotationEffect(.degrees(270.0))
                  }.frame(height: UIScreen.main.bounds.height * 0.3)
                  Text("Tap anywhere to go back")
                      .font(.system(size: 20, weight: .bold))

              }
              .frame(width: UIScreen.main.bounds.width)
              .background(Color.init(hex: "80ffffff"))
                
        }
      

    }
    
}

