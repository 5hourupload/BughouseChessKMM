import SwiftUI
import shared

struct GameView: View {
    @ObservedObject var gameManager: GameManager

    init() {
        self.gameManager =  GameManager(counter: 0)
    }

    var body: some View {
 
        
        HStack(spacing: 0) {
            ForEach((0...7), id: \.self) { y in
                VStack(spacing: 0) {
                    ForEach(0...7, id: \.self) { x in
                        SquareView(x: x, y: y, boardNumber: 0, gameManager: gameManager).onTapGesture {
                            gameManager.processMove(x: x,y: y,boardNumber: 0)
                        }
                    }
                }
            }
        }
        

        }
    }

    
