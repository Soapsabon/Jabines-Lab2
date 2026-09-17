import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [products, setProducts] = useState([])
  const [selectedProduct, setSelectedProduct] = useState('')
  const [quantity, setQuantity] = useState(1)
  const [result, setResult] = useState(null)
  const [inventory, setInventory] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  // Load inventory on mount
  useEffect(() => {
    loadInventory()
  }, [])

  const loadInventory = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/inventory')
      if (!response.ok) throw new Error('Failed to load inventory')
      const data = await response.json()
      setInventory(data)
      setProducts(data)
      if (data.length > 0 && !selectedProduct) {
        setSelectedProduct(data[0].productId)
      }
    } catch (err) {
      setError('Failed to load inventory: ' + err.message)
    }
  }

  const handlePlaceOrder = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')
    setResult(null)

    try {
      const response = await fetch('http://localhost:8080/api/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          productId: selectedProduct,
          quantity: parseInt(quantity)
        })
      })

      const data = await response.json()
      setResult(data)
      setInventory(data.inventory)
    } catch (err) {
      setError('Error placing order: ' + err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container">
      <header className="header">
        <h1>CAMPUS SHOP</h1>
        <p>Order & Inventory System</p>
      </header>

      <main className="main">
        <section className="order-section">
          <h2>Place Order</h2>
          <form onSubmit={handlePlaceOrder}>
            <div className="form-group">
              <label>Product</label>
              <select 
                value={selectedProduct} 
                onChange={(e) => setSelectedProduct(e.target.value)}
                disabled={loading}
              >
                {products.map(p => (
                  <option key={p.productId} value={p.productId}>
                    {p.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Quantity</label>
              <input 
                type="number" 
                min="1" 
                value={quantity} 
                onChange={(e) => setQuantity(e.target.value)}
                disabled={loading}
              />
            </div>

            <button type="submit" disabled={loading}>
              {loading ? 'Processing...' : 'PLACE ORDER'}
            </button>
          </form>
        </section>

        {error && <div className="error">{error}</div>}

        {result && (
          <section className="result-section">
            <h2>Order Result</h2>
            <div className={`result ${result.status.toLowerCase()}`}>
              <span className="status-icon">
                {result.status === 'CONFIRMED' ? '✓' : '✕'}
              </span>
              <div className="status-text">
                <strong>{result.status}</strong>
                <p>{result.reason}</p>
              </div>
            </div>
          </section>
        )}

        <section className="inventory-section">
          <h2>Inventory</h2>
          <table className="inventory-table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Stock</th>
              </tr>
            </thead>
            <tbody>
              {inventory.map(item => (
                <tr key={item.productId}>
                  <td>{item.name}</td>
                  <td className={item.stock < 5 ? 'low-stock' : ''}>
                    {item.stock}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      </main>
    </div>
  )
}

export default App
