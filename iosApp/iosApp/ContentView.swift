import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greeting()
    
	var body: some View {
		Text(greet)

        GameView()

        }
	}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}



extension UIImage {
    
    func resizeImageTo(size: CGSize) -> UIImage? {
        
        UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
        self.draw(in: CGRect(origin: CGPoint.zero, size: size))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return resizedImage
    }
}

