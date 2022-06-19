//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// To contact the authors:
// http://www.inf.ufrgs.br/~bordini
// http://www.das.ufsc.br/~jomi
//
//----------------------------------------------------------------------------

import br.pro.turing.javino.Javino;
import jason.architecture.AgArch;

/**
 * Base agent architecture class that defines the overall agent architecture;
 * the AS interpreter is the reasoner (a kind of mind) within this architecture
 * (a kind of body).
 *
 * <p>
 * The agent reasoning cycle (implemented in TransitionSystem class) calls these
 * methods to get perception, action, and communication.
 *
 * <p>
 * This class implements a Chain of Responsibilities design pattern. Each member
 * of the chain is a subclass of AgArch. The last arch in the chain is the
 * infrastructure tier (Centralised, JADE, Saci, ...). The getUserAgArch method
 * returns the first arch in the chain.
 * <p>
 * Users can customise the architecture by overriding some methods of this
 * class.
 */
public class Argo extends AgArch {

    public Javino jBridge = new Javino();

    public Argo() {
        this.setPort("COM1");
    }

    public Javino getArgo() {
        return this.jBridge;
    }

}
