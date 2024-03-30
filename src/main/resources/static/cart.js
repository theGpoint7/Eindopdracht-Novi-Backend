// this function provides an unauthorized user (not logged in), to store / add products to their shopping cart
// structure example:
// {
//     productId: 1,
//     quantity: 1
// }

function addToCart(item) {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let existingItem = cart.find(ci => ci.productId === item.productId);
    if (existingItem) {
        existingItem.quantity += item.quantity;
    } else {
        cart.push(item);
    }
    localStorage.setItem('cart', JSON.stringify(cart));
}
