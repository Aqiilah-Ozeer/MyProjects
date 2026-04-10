// script.js (FINAL CHECKED VERSION)

document.addEventListener('DOMContentLoaded', () => {
    const mainButtonsContainer = document.querySelector('.main-category-buttons');
    const subButtonsContainer = document.getElementById('sub-category-buttons-container');
    const petGrid = document.getElementById('pet-grid');

    // Mappings for Main Category (Animal) to Subtypes (Breed) based on your database inserts.
    const subtypeMap = {
        'Cat': [ 'Siamese', 'Maine Coon', 'Bengal', 'Persian', 'Russian Blue', 'Tabby', 'Calico', 'Scottish Fold'], 
        'Dog': [ 'Golden Retriever', 'Beagle', 'Shih Tzu', 'German Shepherd', 'Labrador Retriever', 'Pomeranian', 'Rottweiler', 'Border Collie', 'Boxer', 'Doberman', 'Husky'], 
        'Rabbit': ['Mini Lop', 'Dutch', 'Holland Lop', 'Angora', 'Rex', 'Lionhead'], 
        'Bird': ['Cockatiel', 'Lovebird', 'Budgerigar', 'Parakeet', 'Canary'], 
        '': [] 
    };

    let activeMain = ''; // Starts blank for "All Pets"
    let activeSub = '';

    /**
     * Fetches and displays the pets based on current filters using AJAX.
     */
    function loadPets() {
        petGrid.innerHTML = '<div class="loading-message"><p>Fetching results...</p></div>';

        // Construct the URL with filters
        const url = `fetch_pets.php?main=${activeMain}&sub=${activeSub}`;
        
        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`AJAX Error: Server responded with status ${response.status}.`);
                }
                return response.text();
            })
            .then(html => {
                petGrid.innerHTML = html;
            })
            .catch(error => {
                console.error('Error loading pets:', error);
                // Display error message
                petGrid.innerHTML = `<p class="no-results"> Error: Failed to load pet listings. Check F12 console for details.</p>`;
            });
    }

    /**
     * Updates the subcategory buttons based on the active main category.
     */
    function updateSubCategoryButtons() {
        subButtonsContainer.innerHTML = '<h2>Filter by Subtype/Breed:</h2>';
        const subtypes = subtypeMap[activeMain];

        if (subtypes && subtypes.length > 0) {
            const allSubButton = document.createElement('button');
            allSubButton.textContent = `All ${activeMain} Breeds`;
            allSubButton.classList.add('sub-filter', 'active');
            allSubButton.dataset.sub = '';
            subButtonsContainer.appendChild(allSubButton);

            subtypes.forEach(sub => {
                const button = document.createElement('button');
                button.textContent = sub;
                button.classList.add('sub-filter');
                button.dataset.sub = sub;
                subButtonsContainer.appendChild(button);
            });
        } else {
            const initialText = document.createElement('p');
            initialText.classList.add('initial-sub-text');
            initialText.textContent = activeMain ? `No specific subtypes for ${activeMain}.` : 'Select a species to view available breeds/subtypes.';
            subButtonsContainer.appendChild(initialText);
        }
        
        // Ensure the "All" button for the subtype group is initially active if subtypes exist
        const initialActiveSubButton = subButtonsContainer.querySelector('.sub-filter[data-sub=""]');
        if (initialActiveSubButton) {
            initialActiveSubButton.classList.add('active');
        }
    }

    // --- Event Listeners ---
    mainButtonsContainer.addEventListener('click', (e) => {
        if (e.target.matches('.main-filter')) {
            document.querySelectorAll('.main-filter').forEach(btn => btn.classList.remove('active'));
            e.target.classList.add('active');

            activeMain = e.target.dataset.main;
            activeSub = '';

            updateSubCategoryButtons();
            loadPets();
        }
    });

    subButtonsContainer.addEventListener('click', (e) => {
        if (e.target.matches('.sub-filter')) {
            document.querySelectorAll('.sub-filter').forEach(btn => btn.classList.remove('active'));
            e.target.classList.add('active');

            activeSub = e.target.dataset.sub;
            loadPets();
        }
    });

    // Initial setup to load "All Pets" grid immediately
    updateSubCategoryButtons();
    loadPets(); 
});